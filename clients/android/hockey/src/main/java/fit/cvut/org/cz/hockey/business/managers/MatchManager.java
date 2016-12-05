package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.generators.RoundRobinScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager extends BaseManager<Match> implements IMatchManager {

    public MatchManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<Match, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getHockeyMatchDAO();
    }

    /*private ScoredMatch fillMatch (Context context, Match dm) {
        ScoredMatch match = new ScoredMatch(dm);
        IPackagePlayerManager packagePlayerManager = new PackagePlayerManager();

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, dm.getId());

        for (DParticipant dp : participants) {
            ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, dp.getId());
            int teamGoals = 0;
            for (DStat stat : partStats) {
                if (StatsEnum.valueOf(stat.getStatsEnumId()) == StatsEnum.team_goals) {
                    teamGoals = Integer.parseInt(stat.getValue());
                }
            }

            if (dp.getRole().equals(ParticipantType.home.toString())) {
                match.setHomeParticipantId(dp.getTeamId());
                Team dt = new TeamManager(packagePlayerManager).getById(context, dp.getTeamId());
                match.setHomeName(dt.getName());
                ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, dp.getId());
                match.setHomeIds(playerIds);
                match.setHomeScore(teamGoals);
            }
            if (dp.getRole().equals(ParticipantType.away.toString())) {
                match.setAwayParticipantId(dp.getTeamId());
                Team dt = new TeamManager(packagePlayerManager).getById(context, dp.getTeamId());
                match.setAwayName(dt.getName());
                ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, dp.getId());
                match.setAwayIds(playerIds);
                match.setAwayScore(teamGoals);
            }
        }

        return match;
    }*/

    @Override
    public List<Match> getByTournamentId(Context context, long tournamentId) {
        try {
            List<Match> matches = getDao(context).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            //return fillMatch(context, dm);
            for (Match match : matches) {
                List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(context, match.getId());
                match.addParticipants(participants);

                for (Participant participant : participants) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        match.setHomeScore(ManagerFactory.getInstance(context).participantStatManager.getScoreByParticipantId(context, participant.getId()));
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        match.setAwayScore(ManagerFactory.getInstance(context).participantStatManager.getScoreByParticipantId(context, participant.getId()));
                }
            }
            // TODO match participant stats and match players stats

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
    public Match getById(Context context, long id) {
        Match match = super.getById(context, id);
        //return fillMatch(context, dm);
        // TODO load match stats, match participant stats and match players stats
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(context, id);
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
    public List<Match> getAll(Context context) {
        return null;
    }

    @Override
    public void beginMatch(Context context, long matchId) {
        Match match = getById(context, matchId);
        if (!(match.isPlayed())) {
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(context, match);
        }
    }

    @Override
    public void generateRound(Context context, long tournamentId) {
        List<Team> teamsInTournament = ManagerFactory.getInstance(context).teamManager.getByTournamentId(context, tournamentId);

        Map<Long, Team> teamMap = new HashMap<>();
        ArrayList<fit.cvut.org.cz.tmlibrary.business.entities.Participant> partsForGenerator = new ArrayList<>();

        for (Team team : teamsInTournament) {
            teamMap.put(team.getId(), team);
            // match_id and role will be added by generator
            partsForGenerator.add(new fit.cvut.org.cz.tmlibrary.business.entities.Participant(-1, team.getId(), null));
        }

        int lastRound = 0;

        List<Match> tournMatches = getByTournamentId(context, tournamentId);

        for (Match match : tournMatches) {
            if (match.getRound() > lastRound)
                lastRound = match.getRound();
        }

        IScoredMatchGenerator generator = new RoundRobinScoredMatchGenerator();

        List<fit.cvut.org.cz.tmlibrary.business.entities.Match> matchList = generator.generateRound(partsForGenerator, lastRound + 1);

        for (fit.cvut.org.cz.tmlibrary.business.entities.Match match : matchList) {
            match.setDate(new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            Match hockeyMatch = new Match(match);
            insert(context, hockeyMatch);
            Collection<PlayerStat> playerStats = new ArrayList<>();
            for (fit.cvut.org.cz.tmlibrary.business.entities.Participant participant : match.getParticipants())
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers())
                    playerStats.add(new PlayerStat(participant.getId(), player.getId()));

            for (PlayerStat playerStat : playerStats)
                ManagerFactory.getInstance(context).playerStatManager.insert(context, playerStat);
        }
    }

    @Override
    public void resetMatch(Context context, long matchId) {
        Match match = getById(context, matchId);
        if (!match.isPlayed())
            return;

        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(context, matchId);

        try {
            // Remove Participant and Player Stats
            for (Participant participant : participants) {
                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat, Long> playerStatBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                playerStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                playerStatBuilder.delete();
            }
        } catch (SQLException e) {}

        match.setPlayed(false);
        match.setOvertime(false);
        match.setShootouts(false);
        update(context, match);
    }

    @Override
    public void insert(Context context, Match match) {
        // TODO check if id is filled ?
        match.setLastModified(new Date());
        super.insert(context, match);

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        for (Participant participant : match.getParticipants())
            ManagerFactory.getInstance(context).participantManager.insert(context, participant);
    }

    @Override
    public boolean delete(Context context, long id) {
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(context, id);
        try {
            for (Participant participant : participants) {
                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat, Long> statBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                statBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                statBuilder.delete();
            }
            DeleteBuilder<Participant, Long> participantBuilder = sportDBHelper.getParticipantDAO().deleteBuilder();
            participantBuilder.where().eq(DBConstants.cMATCH_ID, id);
            participantBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        super.delete(context, id);
        return true;
    }
}
