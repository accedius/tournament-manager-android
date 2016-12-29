package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to PointCfgs
 * Created by Vaclav on 3. 4. 2016.
 */
public class PointConfigService extends AbstractIntentServiceWProgress {
    public PointConfigService() {
        super("Squash Point CFG Service");
    }

    public static final String ACTION_EDIT_CFG = "fit.cvut.org.cz.squash.presentation.services.edit_cfg";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_cfg_by_id";

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_EDIT_CFG:{
                PointConfiguration cfg = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
                ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).update(cfg);
                break;
            }
            case ACTION_GET_BY_ID:{
                PointConfiguration cfg = ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                Intent result = new Intent(action);
                result.putExtra(ExtraConstants.EXTRA_CONFIGURATION, cfg);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PointConfigService.class);
        intent.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return intent;
    }
}
