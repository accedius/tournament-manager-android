package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;

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


                break;
            }
            case ACTION_GET_BY_ID:{


                break;
            }
            case ACTION_UPDATE:{


                break;
            }
        }

    }
}
