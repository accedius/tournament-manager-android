package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Intent;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.tmlibrary.business.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Exported service for core
 * Created by atgot_000 on 1. 5. 2016.
 */
public class HockeyService extends AbstractIntentServiceWProgress {
    public HockeyService() {
        super("Hockey Service");
    }

    @Override
    protected String getActionKey() {
        return CrossPackageComunicationConstants.EXTRA_ACTION;
    }


    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(CrossPackageComunicationConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageComunicationConstants.EXTRA_PACKAGE);

        switch (action)
        {
            case CrossPackageComunicationConstants.ACTION_GET_STATS:
            {
                long id = intent.getLongExtra(CrossPackageComunicationConstants.EXTRA_ID, -1);
                Intent res = new Intent(package_name+action);
                AggregatedStatistics ags = ManagerFactory.getInstance().statisticsManager.getByPlayerID(this, id);
                AggregatedStats statsToSend = new AggregatedStats();

                PlayerAggregatedStats as = new PlayerAggregatedStats();
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.g), Long.toString(ags.getGoals()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.a), Long.toString(ags.getAssists()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.p), Long.toString(ags.getPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.pmp), Long.toString(ags.getPlusMinusPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.tp), Long.toString(ags.getTeamPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.gp), Long.toString(ags.getMatches()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.w), Long.toString(ags.getWins()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.d), Long.toString(ags.getDraws()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.l), Long.toString(ags.getLosses()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.i), Long.toString(ags.getInterventions()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ag), String.format("%.2f", ags.getAvgGoals()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ap), String.format("%.2f", ags.getAvgPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.apmp), String.format("%.2f", ags.getAvgPlusMinus()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.atp), String.format("%.2f", ags.getAvgTeamPoints()), false));
                statsToSend.addPlayerStats(as);


                res.putExtra(CrossPackageComunicationConstants.EXTRA_STATS, statsToSend);
                sendBroadcast(res);
                break;
            }
            case CrossPackageComunicationConstants.ACTION_DELETE_COMPETITION:
            {
                Intent res = new Intent(action);
                long compId = intent.getLongExtra(CrossPackageComunicationConstants.EXTRA_ID, -1);
                if( ManagerFactory.getInstance().competitionManager.delete( this, compId) )
                    res.putExtra(CrossPackageComunicationConstants.EXTRA_OUTCOME, CrossPackageComunicationConstants.OUTCOME_OK);
                else
                    res.putExtra(CrossPackageComunicationConstants.EXTRA_OUTCOME, CrossPackageComunicationConstants.OUTCOME_FAILED);
                sendBroadcast( res );
            }
        }

    }
}