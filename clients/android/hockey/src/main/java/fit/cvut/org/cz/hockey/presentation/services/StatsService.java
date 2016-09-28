package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatsService extends AbstractIntentServiceWProgress {
    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_STATS = "extra_stats";
    public static final String EXTRA_STANDINGS = "extra_standings";
    public static final String EXTRA_HOME_STATS = "extra_home_statistics";
    public static final String EXTRA_AWAY_STATS = "extra_away_statistics";
    public static final String EXTRA_HOME_NAME = "extra_home_name";
    public static final String EXTRA_AWAY_NAME = "extra_away_name";

    public static final String ACTION_GET_BY_COMP_ID = "get_by_comp_id";
    public static final String ACTION_GET_BY_TOUR_ID = "get_by_tour_id";
    public static final String ACTION_GET_STANDINGS_BY_TOURNAMENT = "get__standings_by_tour_id";
    public static final String ACTION_GET_MATCH_PLAYER_STATISTICS = "get_match_player_statistics";

    public StatsService() {
        super("Hockey Stats Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, StatsService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action)
        {
            case ACTION_GET_BY_COMP_ID:
            {
                Intent res = new Intent();
                long compID = intent.getLongExtra(EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_COMP_ID);
                ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByCompetitionID(this, compID);

                res.putParcelableArrayListExtra(EXTRA_STATS, stats);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_BY_TOUR_ID:
            {
                Intent res = new Intent();
                long tourID = intent.getLongExtra(EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_TOUR_ID);
                ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getByTournamentID(this, tourID);

                res.putParcelableArrayListExtra(EXTRA_STATS, stats);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_STANDINGS_BY_TOURNAMENT:
            {
                Intent res = new Intent(action);
                long tourID = intent.getLongExtra(EXTRA_ID, -1);
                ArrayList<Standing> standings = ManagerFactory.getInstance().statisticsManager.getStandingsByTournamentId(this, tourID);

                res.putParcelableArrayListExtra(EXTRA_STANDINGS, standings);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_MATCH_PLAYER_STATISTICS:
            {
                Intent res = new Intent(ACTION_GET_MATCH_PLAYER_STATISTICS);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                ScoredMatch match = ManagerFactory.getInstance().matchManager.getById(this, matchId);
                ArrayList<MatchPlayerStatistic> homeStats = new ArrayList<>();
                ArrayList<MatchPlayerStatistic> awayStats = new ArrayList<>();

                if (!match.isPlayed()) {
                    Team homeTeam = ManagerFactory.getInstance().teamManager.getById(this, match.getHomeParticipantId());
                    for (Player p : homeTeam.getPlayers()) homeStats.add(new MatchPlayerStatistic(p.getId(), p.getName(), 0, 0, 0, 0));
                    Team awayTeam = ManagerFactory.getInstance().teamManager.getById(this, match.getAwayParticipantId());
                    for (Player p : awayTeam.getPlayers()) awayStats.add(new MatchPlayerStatistic(p.getId(), p.getName(), 0, 0, 0, 0));
                }
                else
                {
                    for (Long plId : match.getHomeIds())
                        homeStats.add(ManagerFactory.getInstance().statisticsManager.getPlayerStatsInMatch(this, plId, matchId));
                    for (Long plId : match.getAwayIds())
                        awayStats.add(ManagerFactory.getInstance().statisticsManager.getPlayerStatsInMatch(this, plId, matchId));
                }

                res.putParcelableArrayListExtra(EXTRA_HOME_STATS, homeStats);

                res.putParcelableArrayListExtra(EXTRA_AWAY_STATS, awayStats);

                res.putExtra(EXTRA_HOME_NAME, match.getHomeName());
                res.putExtra(EXTRA_AWAY_NAME, match.getAwayName());

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}
