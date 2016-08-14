package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress {
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_COMPETITION = "extra_competition";
    public static final String EXTRA_CONTENT = "extra_content";

    public CompetitionService() {
        super("Competition Service");
    }

    private ArrayList<Competition> getData(String package_name, String content) {
        ArrayList<Competition> data = new ArrayList<>();

        String uri = "content://"+package_name+".data/"+content;
        Uri myUri = Uri.parse(uri);
        Cursor cur = getContentResolver().query(myUri, null, null, null, DBConstants.cSTART + " DESC, " + DBConstants.cEND + " DESC");
        if (cur == null) {
            return data;
        }

        CursorParser cp = CursorParser.getInstance();
        if (cur.moveToFirst()) {
            do {
                data.add(new Competition(cp.parseDCompetition(cur)));
            } while (cur.moveToNext());
        }
        return data;
    }

    public static Intent getStartIntent(String action, String package_name, String content, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);
        intent.putExtra(EXTRA_PACKAGE, package_name);
        intent.putExtra(EXTRA_CONTENT, content);
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
        String content = intent.getStringExtra(EXTRA_CONTENT);

        Intent result = new Intent(action);
        result.putParcelableArrayListExtra(EXTRA_COMPETITION, getData(package_name, content));
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
