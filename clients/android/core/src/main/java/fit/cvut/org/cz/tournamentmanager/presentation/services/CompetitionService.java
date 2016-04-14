package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_ALL = "get_all";

    public static final String EXTRA_PLAYER_ID = "extra_player_id";
    public static final String EXTRA_LIST = "extra_list";

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_RESULT = "extra_result";

    public CompetitionService() {
        super("Competition Service");
    }

    private ArrayList<Competition> getData(String package_name) {
        ArrayList<Competition> data = new ArrayList<>();

        Uri myUri = Uri.parse("content://"+package_name+".data/competitions");
        Cursor cur = getContentResolver().query(myUri, null, null, null, null);
        if (cur == null) {
            return data;
        }

        Log.d("DB", "Total rows " + cur.getCount());
        CursorParser cp = CursorParser.getInstance();
        if (cur.moveToFirst()) {
            do {
                data.add(new Competition(cp.parseDCompetition(cur)));
            } while (cur.moveToNext());
        }
        return data;
    }

    public static Intent getStartIntent(String action, String package_name, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);
        intent.putExtra(EXTRA_PACKAGE, package_name);
        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        String package_name = intent.getStringExtra(EXTRA_PACKAGE);
        Intent result = new Intent(action);

        result.putParcelableArrayListExtra(EXTRA_RESULT, getData(package_name));
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
