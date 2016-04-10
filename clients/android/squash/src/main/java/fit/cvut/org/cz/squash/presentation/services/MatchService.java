package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public class MatchService extends AbstractIntentServiceWProgress {

    public MatchService() {
        super("Squash Match Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_MATCHES = "extra_matches";

    public static final String ACTION_GET_MATCHES_BY_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_matches_by_tournament";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (action){
            case ACTION_GET_MATCHES_BY_TOURNAMENT:{

                Intent result = new Intent(action);
                result.putExtra(EXTRA_MATCHES, ManagersFactory.getInstance().matchManager.getByTournamentId(intent.getLongExtra(EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        }


    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, MatchService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
