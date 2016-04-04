package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by kevin on 4.4.2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_RESULT = "extra_result";

    public PlayerService() {
        super("Competition Service");
    }

    private ArrayList<Competition> getData() {
        ArrayList<Competition> data = new ArrayList<>();

        Uri myUri = Uri.parse("content://fit.cvut.org.cz.tournamentmanager.data/players");
        Cursor cur = getContentResolver().query(myUri, null, null, null, null);
        if (cur == null) {
            return data;
        }
        else {
            Log.d("DB", "Total rows " + cur.getCount());
            if (cur.moveToFirst()) {
                do {
                    data.add(new Competition(cur));
                } while (cur.moveToNext());
            }
        }

        return data;
    }

    public static Intent getStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(EXTRA_ACTION, action);
        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        Intent result = new Intent(action);

        result.putParcelableArrayListExtra(EXTRA_RESULT, getData());
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
