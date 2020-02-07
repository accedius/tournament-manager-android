package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Bowling point configuration service to handle intent/service/activity work in point configuration's scope
 */
public class PointConfigurationService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_ID = "action_get_config_by_id";
    public static final String ACTION_INSERT = "action_insert_config";
    public static final String ACTION_EDIT = "action_edit_config";
    public static final String ACTION_DELETE = "action_delete_config";
    public static final String ACTION_GET_CONFIGS_BY_TOURNAMENT = "action_get_configs_by_tournament";

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, PointConfigurationService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    public PointConfigurationService() {
        super("Bowling Point Configuration Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    /**
     * sends point configurations to broadcast
     * @param id id of tournament
     */
    private void sendPointConfigurations(long id) {
        Intent res = new Intent(ACTION_GET_CONFIGS_BY_TOURNAMENT);
        List<PointConfiguration> configurations = ((IPointConfigurationManager) ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class)).getByTournamentId(id);
        res.putParcelableArrayListExtra(ExtraConstants.EXTRA_CONFIGURATIONS, new ArrayList<>(configurations));

        LocalBroadcastManager.getInstance(this).sendBroadcast(res);
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_ID: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                PointConfiguration pc = ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).getById(id);
                Intent res = new Intent(ACTION_GET_BY_ID);
                res.putExtra(ExtraConstants.EXTRA_CONFIGURATION, pc);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_INSERT: {
                PointConfiguration pc = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
                ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).insert(pc);
                sendPointConfigurations(pc.getTournamentId());
                break;
            }
            case ACTION_EDIT: {
                PointConfiguration pc = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
                ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).update(pc);
                sendPointConfigurations(pc.getTournamentId());
                break;
            }
            case ACTION_GET_CONFIGS_BY_TOURNAMENT: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                sendPointConfigurations(id);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(ACTION_DELETE);
                long PointConfigurationId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                if (PointConfigurationId == -1)
                    break;
                boolean result = ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).delete(PointConfigurationId);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                res.putExtra(ExtraConstants.EXTRA_POSITION, intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}