package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

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
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */



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

                //TODO prevest na jadrovou formu

                // Ukazka posilani statistik
                AggregatedStats as = new AggregatedStats();
                as.addRecord(new AggregatedStatsRecord("G", "10"));
                as.addRecord(new AggregatedStatsRecord("A", "5"));
                as.addRecord(new AggregatedStatsRecord("Points", "8"));
                res.putExtra(CrossPackageComunicationConstants.EXTRA_STATS, as);

                sendBroadcast( res );
                break;
            }
        }

    }
}