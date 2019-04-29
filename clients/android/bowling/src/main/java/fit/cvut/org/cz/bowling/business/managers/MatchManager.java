package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;

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
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

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
                        match.setHomeScore(((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getScoreByParticipantId(participant.getId()));
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        match.setAwayScore(((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getScoreByParticipantId(participant.getId()));
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
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Match getById(long id) {
        Match match = super.getById(id);
        List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(id);
        for (Participant participant : participants) {
            match.addParticipant(participant);
            if (ParticipantType.home.toString().equals(participant.getRole()))
                match.setHomeName(participant.getName());
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                match.setAwayName(participant.getName());
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
        List<Team> teams = ((ITeamManager)managerFactory.getEntityManager(Team.class)).getByTournamentId(tournamentId);
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

        List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(matchId);

        try {
            // Remove Participant Stats and reset Player Stats
            for (Participant participant : participants) {
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                List<PlayerStat> stats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class)
                        .queryForEq(DBConstants.cPARTICIPANT_ID, participant.getId());
                for (PlayerStat stat : stats) {
                    stat.setGoals(0);
                    stat.setAssists(0);
                    stat.setPlusMinus(0);
                    stat.setSaves(0);
                    managerFactory.getDaoFactory().getMyDao(PlayerStat.class).update(stat);
                }
            }
        } catch (SQLException e) {}

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
}
