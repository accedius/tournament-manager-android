package fit.cvut.org.cz.bowling.business.managers;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class MatchManager extends BaseManager<Match> implements IMatchManager {
    @Override
    protected Class<Match> getMyClass() {
        return Match.class;
    }

    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        IEntityDAO<Match, Long> matchDAO = managerFactory.getDaoFactory().getMyDao(Match.class);
        List<Match> matches = matchDAO.getListItemById(DBConstants.cTOURNAMENT_ID, tournamentId);
        for (Match match : matches) {
            IParticipantManager participantManager = (managerFactory.getEntityManager(Participant.class));
            List<Participant> participants = participantManager.getByMatchId(match.getId());
            match.addParticipants(participants);
            for (Participant participant : participants) {

                //Add player stats
                List<PlayerStat> playerStats = ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
                participant.setPlayerStats(playerStats);
            }
        }

        Collections.sort(matches, new Comparator<Match>() {
                @Override
                public int compare(Match lhs, Match rhs) {
                    if (lhs.getRound() != rhs.getRound()) return lhs.getRound() - rhs.getRound();
                    return lhs.getPeriod() - rhs.getPeriod();
                }
            });
        return matches;
    }

    @Override
    public Match getById(long id) {
        Match match = super.getById(id);
        IParticipantManager participantManager = (managerFactory.getEntityManager(Participant.class));
        List<Participant> participants = participantManager.getByMatchId(id);
        for (Participant participant : participants) {
            match.addParticipant(participant);
        }
        return match;
    }

    @Override
    public Match getByIdFromDao(long matchId) {
        return super.getById(matchId);
    }

    @Override
    public void beginMatch(long matchId) {
        Match match = getById(matchId);
        if (!(match.isPlayed())) {
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(match);
        }
    }

    @Override
    public void generateByLanes(long tournamentId, int lanes) {
        if(lanes < 1) {
            return;
        }

        ITournamentManager iTournamentManager = managerFactory.getEntityManager(Tournament.class);
        List<Player> players = iTournamentManager.getTournamentPlayers(tournamentId);
        ArrayList<fit.cvut.org.cz.tmlibrary.data.entities.Match> matches = new ArrayList<>();
        
        // List of all matches
        List<Match> matchList = getByTournamentId(tournamentId);

        // Find all matches in last round
        List<Match> lastMatchList = new LinkedList<>();
        long maxRoundId = 0;
        for(Match match : matchList) {
            if(match.getRound() > maxRoundId) {
                lastMatchList.clear();
                maxRoundId = match.getRound();
            }

            if(match.getRound() == maxRoundId) {
                lastMatchList.add(match);
            }
        }

        // Discover who played against who
        Map<Long, Set<Long>> playedAgainst = new HashMap<>();

        Set<Long> tmpPlayers = new HashSet<>();

        for(Match match : lastMatchList) {
            tmpPlayers.clear();

            for(Participant participant : match.getParticipants()) {
                for(fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat playerStat : participant.getPlayerStats()) {
                    tmpPlayers.add(playerStat.getPlayerId());
                }
            }

            for(Long id : tmpPlayers) {
                Set<Long> against = playedAgainst.get(id);

                if(against == null) {
                    against = new HashSet<>();
                    playedAgainst.put(id, against);
                }

                against.addAll(tmpPlayers);
            }
        }

        // Figure out placement of players on all lanes
        boolean newCombination = false;
        List<List<Long>> lanePlayers = new LinkedList<>();
        for(int i = 0; i < lanes; ++i) {
            List<Long> currentLane = new LinkedList<Long>();

            int split = players.size() / (lanes - i);

            // In case of lanes > players
            if(players.size() == 1) {
                split = 1;
            } else if(players.size() < 1) {
                break;
            }

            //For each empty 'spot' on lane
            int firstBorn = (int)(Math.random() * (players.size())) % players.size();
            currentLane.add(players.get(firstBorn).getId());
            players.remove(firstBorn);
            for(int j = 1; j < split; ++j) {
                //For each player already inside this lane
                for(Long pid : currentLane) {
                    // Try to find someone he hasn't played with
                    for(Player candidate : players) {
                        //Candidate hasn't played any match against pid
                        Set<Long> pag = playedAgainst.get(pid);
                        if(pag == null || !pag.contains(candidate.getId())) {
                            currentLane.add(candidate.getId());
                            players.remove(candidate);
                            newCombination = true;
                            break;
                        }
                    }

                    //Player was picked for this spot, no need to pick another one
                    if(currentLane.size() > j) {
                        break;
                    }
                }

                //We couldn't find any player, let's throw in some random weirdo
                if(currentLane.size() == j) {
                    currentLane.add(players.get(0).getId());
                    players.remove(0);
                }
            }

            lanePlayers.add(currentLane);
            //Log.d("Lane de Bug", "added new lane, current number is "+lanePlayers.size());
        }

        // Find which period and round is next
        //TODO rewrite
        int targetPeriod = 0;
        int targetRound = lastMatchList.isEmpty() ? 1 : (lastMatchList.get(0).getRound() + (newCombination ? 0 : 1));
        for(Match m : lastMatchList) {
            if(m.getPeriod() > targetPeriod) {
                targetPeriod = m.getPeriod();
            }
        }
        targetPeriod++;

        if(!newCombination) {
            targetPeriod = 1;
        }

        //TODO merge this and next for cycles into one, merge their inner for cycles, there it's possible
        //Create entities
        //Log.d("Lane de Bug", "we want " + lanes + " lanes and we have " + lanePlayers.size() + " generated");
        for(int i = 0 ; i < lanes ; i++) {

            //Make participants from players (tournamentType == Individuals)
            List<Participant> currentLaneParticipants = new ArrayList<>();
            if (i < lanePlayers.size()) {
                List<Long> currentLanePlayerIds = lanePlayers.get(i);
                for (Long currentLanePlayerId : currentLanePlayerIds) {
                    Participant p = new Participant(-1, currentLanePlayerId, null);

                    currentLaneParticipants.add(p);
                }
            }

            fit.cvut.org.cz.tmlibrary.data.entities.Match match = new fit.cvut.org.cz.tmlibrary.data.entities.Match();
            match.setPeriod(targetPeriod);
            match.setRound(targetRound);
            match.addParticipants(currentLaneParticipants);
            matches.add(match);
        }

        for (fit.cvut.org.cz.tmlibrary.data.entities.Match match : matches) {
            match.setDate(new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            Match bowlingMatch = new Match(match);
            insert(bowlingMatch);

            List<Participant> participants = match.getParticipants();
            List<PlayerStat> playerStats = new ArrayList<>();
            //TODO consider future changes on what playerstats and participants are
            //TODO ~DONE, to understand view entities and managers or contact Alex
            for(Participant participant : participants) {
                PlayerStat playerStat = new PlayerStat(participant.getId(), participant.getParticipantId());
                //Log.d("Lane de Bug", "participant " + participant.getId() + " and " + participant.getParticipantId() + "/" + playerStat.getPlayerId());
                playerStats.add(playerStat);
            }

            /* Tohle urcite ne
            Participant participant = bowlingMatch.getParticipants().get(0);
            for(Long id : lanePlayers.get(0)) {
                playerStats.add(new PlayerStat(participant.getId(), id));
            }
            lanePlayers.remove(0);
            */

            for (PlayerStat playerStat : playerStats) {
                managerFactory.getEntityManager(PlayerStat.class).insert(playerStat);
            }
        }
    }

    @Override
    public void generateRound(long tournamentId) {
        ITeamManager iTeamManager = managerFactory.getEntityManager(Team.class);
        List<Team> teams = iTeamManager.getByTournamentId(tournamentId);
        Map<Long, Team> teamMap = new HashMap<>();
        ArrayList<Participant> partsForGenerator = new ArrayList<>();

        for (Team team : teams) {
            teamMap.put(team.getId(), team);
            // match_id and role will be added by generator
            partsForGenerator.add(new Participant(-1, team.getId(), null));
        }

        int lastRound = 0;
        List<Match> matches = getByTournamentId(tournamentId);
        for (Match match : matches) {
            if (match.getRound() > lastRound)
                lastRound = match.getRound();
        }

        IMatchGenerator generator = new AllPlayAllMatchGenerator();
        List<fit.cvut.org.cz.tmlibrary.data.entities.Match> matchList = generator.generateRound(partsForGenerator, lastRound + 1);

        for (fit.cvut.org.cz.tmlibrary.data.entities.Match match : matchList) {
            match.setDate(new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            Match bowlingMatch = new Match(match);
            insert(bowlingMatch);
            List<PlayerStat> playerStats = new ArrayList<>();
            for (Participant participant : match.getParticipants())
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers())
                    playerStats.add(new PlayerStat(participant.getId(), player.getId()));

            for (PlayerStat playerStat : playerStats)
                managerFactory.getEntityManager(PlayerStat.class).insert(playerStat);
        }
    }

    @Override
    public void resetMatch(long matchId) {
        Match match = getById(matchId);

        /*if (!match.isPlayed())
            return;*/

        IParticipantManager iParticipantManager = managerFactory.getEntityManager(Participant.class);
        List<Participant> participants = iParticipantManager.getByMatchId(matchId);

        try {
            // Remove Participant Stats and reset Player Stats
            IEntityDAO<ParticipantStat, Long> participantStatDAO = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class);
            IEntityDAO<PlayerStat, Long> playerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);
            IFrameManager iFrameManager = managerFactory.getEntityManager(Frame.class);
            iFrameManager.deleteAllByMatchId(matchId);
            for (Participant participant : participants) {
                participantStatDAO.deleteItemById(DBConstants.cPARTICIPANT_ID, participant.getId());
                List<PlayerStat> stats = playerStatDAO.getListItemById(DBConstants.cPARTICIPANT_ID, participant.getId());
                for (PlayerStat stat : stats) {
                    stat.setStrikes(0);
                    stat.setSpares(0);
                    stat.setPoints(0);
                    playerStatDAO.updateItem(stat);
                }
            }
        } catch (SQLException e) {} //SQL exception je jenom nazev/class chyby, nema nic spolecneho s implementaci

        match.setPlayed(false);
        match.setTrackRolls(false);
        match.setValidForStats(false);
        update(match);
    }

    @Override
    public void insert(Match match) {
        match.setLastModified(new Date());
        super.insert(match);

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        for (Participant participant : match.getParticipants())
            managerFactory.getEntityManager(Participant.class).insert(participant);
    }

    /**
     * Delete participants and frames
     * @param id    match id
     */
    public boolean deleteContents(long id) {
        IParticipantManager iParticipantManager = managerFactory.getEntityManager(Participant.class);
        List<Participant> participants = iParticipantManager.getByMatchId(id);

        IFrameManager iFrameManager = managerFactory.getEntityManager(Frame.class);
        iFrameManager.deleteAllByMatchId(id);

        try {
            IEntityDAO<ParticipantStat, Long> ParticipantStatDAO = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class);
            IEntityDAO<PlayerStat, Long> PlayerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);

            for (Participant participant : participants) {
                ParticipantStatDAO.deleteItemById(DBConstants.cPARTICIPANT_ID, participant.getId());

                PlayerStatDAO.deleteItemById(DBConstants.cPARTICIPANT_ID, participant.getId());
            }
            IEntityDAO<Participant, Long> participantDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
            participantDAO.deleteItemById(DBConstants.cMATCH_ID, id);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(long id) {
        if(deleteContents(id)) {
            super.delete(id);
            return true;
        }
        return false;
    }
}
