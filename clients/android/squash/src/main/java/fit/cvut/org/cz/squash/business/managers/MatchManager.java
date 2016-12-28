package fit.cvut.org.cz.squash.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager extends BaseManager<Match> implements IMatchManager {
    @Override
    protected Class<Match> getMyClass() {
        return Match.class;
    }

    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            List<Match> matches = managerFactory.getDaoFactory().getMyDao(Match.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Match match : matches) {
                List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(match.getId());
                match.addParticipants(participants);

                for (Participant participant : participants) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        match.setHomeName(getParticipantName(match, participant));
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        match.setAwayName(getParticipantName(match, participant));
                }
                loadMatchScore(match);
            }

            Collections.sort(matches, new Comparator<Match>() {
                @Override
                public int compare(Match lhs, Match rhs) {
                    if (lhs.getRound() != rhs.getRound()) return lhs.getRound() - rhs.getRound();
                    return lhs.getPeriod() - rhs.getPeriod();
                }
            });
            return matches;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getParticipantName(Match match, Participant participant) {
        Tournament tournament = managerFactory.getEntityManager(Tournament.class).getById(match.getTournamentId());
        Competition competition = managerFactory.getEntityManager(Competition.class).getById(tournament.getCompetitionId());
        if (CompetitionTypes.teams().equals(competition.getType())) {
            Team team = managerFactory.getEntityManager(Team.class).getById(participant.getParticipantId());
            return team.getName();
        } else {
            Player player = managerFactory.getEntityManager(Player.class).getById(participant.getParticipantId());
            return player.getName();
        }
    }

    @Override
    public Match getById(long id) {
        Match match = super.getById(id);
        if (match == null)
            return match;

        List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(id);
        for (Participant participant : participants) {
            match.addParticipant(participant);

            if (ParticipantType.home.toString().equals(participant.getRole()))
                match.setHomeName(getParticipantName(match, participant));
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                match.setAwayName(getParticipantName(match, participant));
        }
        loadMatchScore(match);
        return match;
    }

    @Override
    public void generateRound(long tournamentId) {
        Tournament tournament = managerFactory.getEntityManager(Tournament.class).getById(tournamentId);
        Competition competition = managerFactory.getEntityManager(Competition.class).getById(tournament.getCompetitionId());
        CompetitionType type = competition.getType();

        ArrayList<Participant> partsForGenerator = new ArrayList<>();

        if (CompetitionTypes.individuals().equals(type)) {
            List<Player> players = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getTournamentPlayers(tournamentId);
            for (Player player : players) {
                // match_id and role will be added by generator
                partsForGenerator.add(new Participant(-1, player.getId(), null));
            }
        } else {
            List<Team> teams = ((ITeamManager)managerFactory.getEntityManager(Team.class)).getByTournamentId(tournamentId);
            for (Team team : teams) {
                // match_id and role will be added by generator
                partsForGenerator.add(new Participant(-1, team.getId(), null));
            }
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
            Match squashMatch = new Match(match);
            insert(squashMatch);
        }
    }

    @Override
    public void resetMatch(long matchId) {
        Match match = getById(matchId);
        if (!match.isPlayed())
            return;

        List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(matchId);

        try {
            // Remove Participant Stats
            for (Participant participant : participants) {
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();
            }
        } catch (SQLException e) {}

        match.setPlayed(false);
        match.setSetsNumber(0);
        update(match);
    }

    @Override
    public void insert(Match match) {
        match.setLastModified(new Date());

        Tournament tournament = managerFactory.getEntityManager(Tournament.class).getById(match.getTournamentId());
        if (tournament == null)
            return;
        Competition competition = managerFactory.getEntityManager(Competition.class).getById(tournament.getCompetitionId());
        if (competition == null)
            return;
        super.insert(match);

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        for (Participant participant : match.getParticipants())
            managerFactory.getEntityManager(Participant.class).insert(participant);

        Map<Long, Team> teamMap = new HashMap<>();
        List<Team> teams = ((ITeamManager)managerFactory.getEntityManager(Team.class)).getByTournamentId(match.getTournamentId());
        for (Team team : teams) {
            teamMap.put(team.getId(), team);
        }

        List<PlayerStat> playerStats = new ArrayList<>();
        for (Participant participant : match.getParticipants()) {
            if (CompetitionTypes.individuals().equals(competition.getType())) {
                playerStats.add(new PlayerStat(participant.getId(), participant.getParticipantId()));
            } else {
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers())
                    playerStats.add(new PlayerStat(participant.getId(), player.getId()));
            }
        }

        for (PlayerStat playerStat : playerStats)
            managerFactory.getEntityManager(PlayerStat.class).insert(playerStat);
    }

    @Override
    public boolean delete(long id) {
        List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(id);
        try {
            for (Participant participant : participants) {
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<PlayerStat, Long> statBuilder = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).deleteBuilder();
                statBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                statBuilder.delete();
            }
            DeleteBuilder<Participant, Long> participantBuilder = managerFactory.getDaoFactory().getMyDao(Participant.class).deleteBuilder();
            participantBuilder.where().eq(DBConstants.cMATCH_ID, id);
            participantBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        super.delete(id);
        return true;
    }

    @Override
    public void updateMatch(long matchId, ArrayList<SetRowItem> sets) {
        Match match = getById(matchId);
        // Delete all sets in match
        ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).deleteByMatchId(matchId);

        if (sets.isEmpty()) {
            match.setSetsNumber(sets.size());
            update(match);
            return;
        }

        // Add new sets
        List<ParticipantStat> stats = new ArrayList<>();
        match.setSetsNumber(sets.size());
        match.setPlayed(true);
        update(match);

        int i=1;
        for (SetRowItem set : sets) {
            for (Participant participant : match.getParticipants()) {
                if (ParticipantType.home.toString().equals(participant.getRole()))
                    stats.add(new ParticipantStat(participant.getId(), i, set.getHomeScore()));
                else if (ParticipantType.away.toString().equals(participant.getRole()))
                    stats.add(new ParticipantStat(participant.getId(), i, set.getAwayScore()));
            }
            i++;
        }

        for (ParticipantStat stat : stats)
            managerFactory.getEntityManager(ParticipantStat.class).insert(stat);
    }

    private void loadMatchScore(Match match) {
        if (!match.isPlayed())
            return;

        Participant home = null, away = null;
        for (Participant participant : match.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                home = participant;
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                away = participant;
            }
        }

        List<ParticipantStat> homeStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(home.getId());
        List<ParticipantStat> awayStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(away.getId());
        Map<Integer, Integer> homePointsMap = new HashMap<>();
        Map<Integer, Integer> awayPointsMap = new HashMap<>();

        home.setParticipantStats(homeStats);
        away.setParticipantStats(awayStats);

        for (ParticipantStat stat : homeStats) {
            homePointsMap.put(stat.getSetNumber(), stat.getPoints());
        }
        for (ParticipantStat stat : awayStats) {
            awayPointsMap.put(stat.getSetNumber(), stat.getPoints());
        }

        int homeWonSets = 0;
        int awayWonSets = 0;
        for (int i=1; i <= match.getSetsNumber(); i++) {
            if (homePointsMap.get(i) > awayPointsMap.get(i))
                homeWonSets++;
            else if (homePointsMap.get(i) < awayPointsMap.get(i))
                awayWonSets++;
        }
        match.setHomeWonSets(homeWonSets);
        match.setAwayWonSets(awayWonSets);
    }
}
