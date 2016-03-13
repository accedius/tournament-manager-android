package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionService extends IntentService {

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_RESULT = "extra_result";

    public CompetitionService() {
        super("Competition Service");
    }

    private ArrayList<Competition> getData() {

        ArrayList<Competition> data = new ArrayList<>();
        data.add(new Competition(1, null, "Prvni liga", new Date(2013,1,1), new Date(2013,12,31), null, null, null, "squash"));
        data.add(new Competition(2, null, "Vikendova liga", new Date(2013,1,1), new Date(2013,6, 30), null, null, null, "hockey"));
        data.add(new Competition(3, null, "Mistrovstvi Prahy", new Date(2015, 3, 2), new Date(2015,3,5), null, null, null, "squash"));

        return data;
    }

    public static Intent getStartIntent(String action, Context context){

        Intent intent = new Intent(context, CompetitionService.class);
        intent.setAction(action);

        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String action = intent.getAction();
        Intent result = new Intent(action);

        result.putParcelableArrayListExtra(EXTRA_RESULT, getData());

        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
