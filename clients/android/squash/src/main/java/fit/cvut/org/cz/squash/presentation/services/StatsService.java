package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.business.ManagersFactory;
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
                result.putExtra(EXTRA_STATS, ManagersFactory.getInstance().statsManager.getAggregatedStatsByCompetitionId(this, intent.getLongExtra(EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_STATS_BY_TOURNAMENT:{
                Intent result = new Intent(action);
                result.putExtra(EXTRA_STATS, ManagersFactory.getInstance().statsManager.getAggregatedStatsByTournamentId(this, intent.getLongExtra(EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_STANDINGS:{
                Intent result = new Intent(action);
                result.putExtra(EXTRA_STATS, ManagersFactory.getInstance().statsManager.getStandingsByTournament(this, intent.getLongExtra(EXTRA_ID, -1)));

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
