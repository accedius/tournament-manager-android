package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress {

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_RESULT = "extra_result";


    public CompetitionService() {
        super("Competition Service");
    }

    private ArrayList<Competition> getData() {

        ArrayList<Competition> data = new ArrayList<>();
        data.add(new Competition(1, null, "Prvni liga", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), null, null, null, "squash"));
        data.add(new Competition(2, null, "Vikendova liga",Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), null, null, null, "hockey"));
        data.add(new Competition(3, null, "Mistrovstvi Prahy", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), null, null, null, "squash"));

        return data;
    }

    public static Intent getStartIntent(String action, Context context){

        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String action = intent.getStringExtra(EXTRA_ACTION);
        Intent result = new Intent(action);

        result.putParcelableArrayListExtra(EXTRA_RESULT, getData());

        LocalBroadcastManager.getInstance(this).sendBroadcast(result);

    }
}
