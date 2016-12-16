package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Statistics
 * Created by Vaclav on 3. 4. 2016.
 */
public class StatsService extends AbstractIntentServiceWProgress {
    public StatsService() {
        super("Squash Stats Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_STATS = "extra_stats";

    public static final String ACTION_GET_STATS_BY_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.get_stats_by_competition";
    public static final String ACTION_GET_STATS_BY_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_stats_by_tournament";
    public static final String ACTION_GET_STANDINGS = "fit.cvut.org.cz.squash.presentation.services.get_standings";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_GET_STATS_BY_COMPETITION:{
                Intent result = new Intent(action);
                List<SAggregatedStats> stats = ((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(SAggregatedStats.class)).getByCompetitionId(intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_STATS, new ArrayList<>(stats));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_STATS_BY_TOURNAMENT:{
                Intent result = new Intent(action);
                ArrayList<SAggregatedStats> stats = new ArrayList<>(((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(SAggregatedStats.class))
                        .getByTournamentId(intent.getLongExtra(EXTRA_ID, -1)));
                result.putExtra(EXTRA_STATS, stats);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_STANDINGS:{
                try { Thread.sleep(200); } catch (InterruptedException e) {} // loading is faster then e.g. deleting
                Intent result = new Intent(action);
                ArrayList<StandingItem> standingItems = new ArrayList<>(((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(SAggregatedStats.class)).getStandingsByTournamentId(intent.getLongExtra(EXTRA_ID, -1)));
                result.putExtra(EXTRA_STATS, standingItems);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, StatsService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
