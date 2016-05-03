package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.tmlibrary.business.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.AggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
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

        switch (action)
        {
            case CrossPackageComunicationConstants.ACTION_GET_STATS:
            {
                Intent res = new Intent(action);
                ArrayList<AggregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getAllAgregated( this );
                ArrayList<AggregatedStats> statsToSend = new ArrayList<>();
                for( AggregatedStatistics ags : stats ){
                    AggregatedStats as = new AggregatedStats();
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.name), ags.getPlayerName()));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.g), Long.toString(ags.getGoals())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.a), Long.toString(ags.getAssists())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.p), Long.toString(ags.getPoints())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.pmp), Long.toString(ags.getPlusMinusPoints())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.tp), Long.toString(ags.getTeamPoints())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.gp), Long.toString(ags.getMatches())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.w), Long.toString(ags.getWins())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.d), Long.toString(ags.getDraws())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.l), Long.toString(ags.getLosses())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.i), Long.toString(ags.getInterventions())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.ag), String.format("%.2f", ags.getAvgGoals())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.ap), String.format("%.2f", ags.getAvgPoints())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.apmp), String.format("%.2f", ags.getAvgPlusMinus())));
                    as.addRecord(new AggregatedStatsRecord(getString(R.string.atp), String.format("%.2f", ags.getAvgTeamPoints())));
                    statsToSend.add( as );
                }
                res.putParcelableArrayListExtra(CrossPackageComunicationConstants.EXTRA_STATS, statsToSend);

                sendBroadcast( res );
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