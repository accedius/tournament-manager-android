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
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.CompetitionDialog;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress {
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_COMPETITION = "extra_competition";
    public static final String EXTRA_RESULT= "extra_result";
    public static final String EXTRA_POSITION= "extra_position";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_SPORT_CONTEXT = "extra_sport_context";
    public static final String EXTRA_TYPE= "extra_type";
    public static final String EXTRA_DELETE= "extra_delete";;

    public CompetitionService() {
        super("Competition Service");
    }

    private boolean deleteCompetition(String package_name, String sport_context, Long competitionId) {
        String uri = "content://"+package_name+".data/"+sport_context+"empty_competition/"+competitionId;
        Uri myUri = Uri.parse(uri);
        Cursor cur = getContentResolver().query(myUri, null, null, null, null);
        if (cur == null || cur.getCount() == 0) {
            return false;
        }

        uri = "content://"+package_name+".data/"+sport_context+"delete_competition/"+competitionId;
        myUri = Uri.parse(uri);
        getContentResolver().delete(myUri, null, null);
        return true;
    }

    private ArrayList<Competition> getCompetitions(String package_name, String content) {
        ArrayList<Competition> data = new ArrayList<>();
        String uri = "content://"+package_name+".data/"+content;
        Uri myUri = Uri.parse(uri);
        Cursor cur = getContentResolver().query(myUri, null, null, null, DBConstants.cSTART + " DESC, " + DBConstants.cEND + " DESC");
        if (cur == null) {
            return data;
        }

        CursorParser cp = CursorParser.getInstance();
        if (cur.getCount() == 0)
            return data;

        cur.moveToFirst();
        do {
            data.add(new Competition(cp.parseDCompetition(cur)));
        } while (cur.moveToNext());
        return data;
    }

    public static Intent getStartIntent(String action, String package_name, String sport_context, String content, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);
        intent.putExtra(EXTRA_PACKAGE, package_name);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(EXTRA_SPORT_CONTEXT, sport_context);
        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String package_name = intent.getStringExtra(EXTRA_PACKAGE);
        String sport_context = intent.getStringExtra(EXTRA_SPORT_CONTEXT);
        String content = intent.getStringExtra(EXTRA_CONTENT);
        String action = intent.getStringExtra(EXTRA_ACTION);

        Intent result = new Intent(action);
        result.putExtra(EXTRA_PACKAGE, package_name);
        result.putExtra(EXTRA_SPORT_CONTEXT, sport_context);
        if (action.equals(CompetitionDialog.ACTION_DELETE_COMPETITION)) {
            result.putExtra(EXTRA_TYPE, EXTRA_DELETE);
            result.putExtra(EXTRA_RESULT, deleteCompetition(package_name, sport_context, Long.parseLong(content)));
            result.putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1));
        } else {
            result.putExtra(EXTRA_TYPE, EXTRA_COMPETITION);
            result.putParcelableArrayListExtra(EXTRA_COMPETITION, getCompetitions(package_name, sport_context + content));
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }
}
