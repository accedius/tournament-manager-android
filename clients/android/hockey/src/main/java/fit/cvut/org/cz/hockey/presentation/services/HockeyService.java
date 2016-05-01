package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
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
                ArrayList<AgregatedStatistics> stats = ManagerFactory.getInstance().statisticsManager.getAllAgregated( this );

                //TODO prevest na jadrovou formu

                sendBroadcast( res );
                break;
            }
        }

    }
}