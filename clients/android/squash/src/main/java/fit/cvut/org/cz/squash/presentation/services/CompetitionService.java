package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Date;

import fit.cvut.org.cz.squash.buisness.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 28. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress{

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_COMPETITION = "extra_competition";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.squash.presentation.services.new_competition";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_competition_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.squash.presentation.services.update_competition";

    public CompetitionService() {
        super("Squash Competition Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{

                Competition c = intent.getParcelableExtra(EXTRA_COMPETITION);

                ManagersFactory.getInstance().competitionManager.insert(this, c);


                break;
            }
            case ACTION_GET_BY_ID:{

                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Competition c = new Competition(intent.getLongExtra(EXTRA_ID, -1), "name3", new Date(2011,3,1), null, "uberNote", CompetitionType.Individuals);
                result.putExtra(EXTRA_COMPETITION, c);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE:{

                Competition c = intent.getParcelableExtra(EXTRA_COMPETITION);


                break;
            }
        }

    }
}
