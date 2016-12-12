package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to PointCfgs
 * Created by Vaclav on 3. 4. 2016.
 */
public class PointConfigService extends AbstractIntentServiceWProgress {
    public PointConfigService() {
        super("Squash Point CFG Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_CFG = "extra_teams";

    public static final String ACTION_EDIT_CFG = "fit.cvut.org.cz.squash.presentation.services.edit_cfg";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_cfg_by_id";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action) {
            case ACTION_EDIT_CFG:{
                PointConfiguration cfg = intent.getParcelableExtra(EXTRA_CFG);
                ManagerFactory.getInstance(this).pointConfigManager.update(this, cfg);
                break;
            }
            case ACTION_GET_BY_ID:{
                PointConfiguration cfg = ManagerFactory.getInstance(this).pointConfigManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                Intent result = new Intent(action);
                result.putExtra(EXTRA_CFG, cfg);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PointConfigService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
