package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class TournamentService extends AbstractIntentServiceWProgress {

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_TOURNAMENT = "extra_tournament";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_LIST = "extra_list";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_create";
    public static final String ACTION_FIND_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.tournament_find_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_update";
    public static final String ACTION_GET_ALL = "fit.cvut.org.cz.hockey.presentation.services.tournament_all";

    public TournamentService() {
        super("Hockey Tournament Service");
    }

    public static Intent newStartIntent(String action, Context context)
    {
        Intent res = new Intent(context, TournamentService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    private ArrayList<Tournament> getData()
    {
        ArrayList<Tournament> res = new ArrayList<>();
        res.add( new Tournament(1, "12345", "Turnaj A", new Date(2011,3,1), new Date(2011,3,2), "----") );
        res.add( new Tournament(1, "12345", "Turnaj B", new Date(2012,3,1), new Date(2012,3,2), "----") );
        res.add( new Tournament(1, "12345", "Turnaj C", new Date(2013,3,1), new Date(2013,3,2), "----") );
        return res;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action)
        {
            case ACTION_CREATE:
            {


                break;
            }
            case ACTION_FIND_BY_ID:
            {


                break;
            }
            case ACTION_UPDATE:
            {


                break;
            }
            case ACTION_GET_ALL:
            {
                Intent res = new Intent();
                res.setAction( ACTION_GET_ALL );
                res.putParcelableArrayListExtra(EXTRA_LIST, getData());
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}
