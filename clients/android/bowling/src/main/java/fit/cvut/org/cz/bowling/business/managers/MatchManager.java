package fit.cvut.org.cz.bowling.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
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
                if (ParticipantType.home.toString().equals(participant.getRole())){
                    IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
                    int participantId = participantStatManager.getScoreByParticipantId(participant.getId());
                    match.setHomeScore(participantId);
                }
                else if (ParticipantType.away.toString().equals(participant.getRole())){
                    IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
                    int participantId = participantStatManager.getScoreByParticipantId(participant.getId());
                    match.setAwayScore(participantId);
                }
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
    public void beginMatch(long matchId) {
        Match match = getById(matchId);
        if (!(match.isPlayed())) {
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(match);
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
            Match hockeyMatch = new Match(match);
            insert(hockeyMatch);
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
        if (!match.isPlayed())
            return;
        IParticipantManager iParticipantManager = managerFactory.getEntityManager(Participant.class);
        List<Participant> participants = iParticipantManager.getByMatchId(matchId);

        try {
            // Remove Participant Stats and reset Player Stats
            IEntityDAO<ParticipantStat, Long> participantStatDAO = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class);
            IEntityDAO<PlayerStat, Long> playerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);;
            for (Participant participant : participants) {
                participantStatDAO.deleteItemById(DBConstants.cPARTICIPANT_ID, participant.getId());
                List<PlayerStat> stats = playerStatDAO.getListItemById(DBConstants.cPARTICIPANT_ID, participant.getId());
                for (PlayerStat stat : stats) {
                    stat.setGoals(0);
                    stat.setAssists(0);
                    stat.setPlusMinus(0);
                    stat.setSaves(0);
                    playerStatDAO.updateItem(stat);
                }
            }
        } catch (SQLException e) {} //SQL exception je jenom nazev/class chyby, nema nic spolecneho s implementaci

        match.setPlayed(false);
        match.setOvertime(false);
        match.setShootouts(false);
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

    @Override
    public boolean delete(long id) {
        IParticipantManager iParticipantManager = managerFactory.getEntityManager(Participant.class);
        List<Participant> participants = iParticipantManager.getByMatchId(id);
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

        super.delete(id);
        return true;
    }
}
