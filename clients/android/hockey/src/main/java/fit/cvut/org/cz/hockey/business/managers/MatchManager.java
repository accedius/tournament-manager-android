package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;
import android.util.Log;

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

import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
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
        ArrayList<Match> res = new ArrayList<>();
        try {
            List<Match> matches = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .query();
            res.addAll(matches);
            //return fillMatch(context, dm);
            // TODO load match stats, match participant stats and match players stats

            Collections.sort(res, new Comparator<Match>() {
                @Override
                public int compare(Match lhs, Match rhs) {
                    if (lhs.getRound() != rhs.getRound()) return lhs.getRound() - rhs.getRound();
                    return lhs.getPeriod() - rhs.getPeriod();
                }
            });
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    public Match getById(Context context, long id) {
        Match match = super.getById(context, id);
        //return fillMatch(context, dm);
        // TODO load match stats, match participant stats and match players stats
        try {
            List<Participant> participants = sportDBHelper.getParticipantDAO().queryBuilder()
                    .where().eq(DBConstants.cMATCH_ID, id).query();
            for (Participant participant : participants) {
                match.addParticipant(participant);
                if (ParticipantType.home.toString().equals(participant.getRole()))
                    match.setHomeName(participant.getName());
                else if (ParticipantType.away.toString().equals(participant.getRole()))
                    match.setAwayName(participant.getName());
            }
            return match;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Match> getAll(Context context) {
        return null;
    }

    @Override
    public void beginMatch(Context context, long matchId) {
        Match match = getById(context, matchId);
        if (!(match.isPlayed())) {
            try {
                Tournament tour = sportDBHelper.getTournamentDAO().queryForId(match.getTournamentId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /*ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
            for (DParticipant dp : participants) {
                for (StatsEnum statEn : StatsEnum.values()) {
                    if (statEn.isForPlayer()) continue;
                    DStat statToAdd = new DStat(-1, -1, dp.getId(), statEn.toString(), match.getTournamentId(), tour.getCompetitionId(), String.valueOf(0));
                    DAOFactory.getInstance().statDAO.insert(context, statToAdd);
                }
            }*/
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(context, match);

            /*DMatchStat matchStat = new DMatchStat(matchId, false, false);
            DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(context, matchStat);*/
        }
    }

    public void generateRound(Context context, long tournamentId) {
        List<Team> teamsInTournament = null;
        try {
            teamsInTournament = sportDBHelper.getTeamDAO().queryBuilder()
                    .where().eq(DBConstants.cTOURNAMENT_ID, tournamentId).query();
        } catch (SQLException e) { return; }

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
            Collection<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat> playerStats = new ArrayList<>();
            for (fit.cvut.org.cz.tmlibrary.business.entities.Participant participant : match.getParticipants()) {
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers()) {
                    playerStats.add(new fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat(participant.getId(), player.getId()));
                }
            }

            try {
                sportDBHelper.getPlayerStatDAO().create(playerStats);
            } catch (SQLException e) {}
        }
    }

    public void resetMatch(Context context, long matchId) {
        Match match = getById(context, matchId);
        if (!match.isPlayed()) return;

        List<Participant> participants = null;
        try {
            participants = sportDBHelper.getParticipantDAO().queryBuilder()
                    .where().eq(DBConstants.cMATCH_ID, matchId).query();

            // Remove Participant and Player Stats
            for (Participant participant : participants) {
                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat, Long> playerStatBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                playerStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                playerStatBuilder.delete();
            }

            match.setPlayed(false);
            match.setOvertime(false);
            match.setShootouts(false);
            update(context, match);
        } catch (SQLException e) {}
    }

    public void insert(Context context, Match match) {
        // TODO check if id is filled
        try {
            match.setLastModified(new Date());
            getDao(context).create(match);
        } catch (SQLException e) {}

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        try {
            sportDBHelper.getParticipantDAO().create(match.getParticipants());
        } catch (SQLException e) {}
    }

    public void update(Context context, Match match) {
        try {
            getDao(context).update(match);
        } catch (SQLException e) {}
    }

    public boolean delete(Context context, long id) {
        try {
            List<Participant> participants = sportDBHelper.getParticipantDAO().queryBuilder()
                    .where().eq(DBConstants.cMATCH_ID, id).query();
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

            sportDBHelper.getMatchDAO().deleteById(id);
            return true;
        } catch (SQLException e) {
            Log.d("MATCH_DELETE", e.getMessage());
            return false;
        }
    }
}
