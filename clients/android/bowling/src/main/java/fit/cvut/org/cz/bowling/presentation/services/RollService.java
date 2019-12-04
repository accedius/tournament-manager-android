package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

public class RollService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_ID = "action_get_by_id";
    public static final String ACTION_INSERT = "action_insert_roll";
    public static final String ACTION_EDIT = "action_edit_roll";
    public static final String ACTION_DELETE = "action_delete_roll";
    public static final String ACTION_GET_BY_FRAME_ID = "action_get_roll_by_frame_id";

    public RollService() {
        super("Bowling Roll Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, RollService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_ID: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Roll roll = ManagerFactory.getInstance(this).getEntityManager(Roll.class).getById(id);
                Intent res = new Intent(ACTION_GET_BY_ID);
                res.putExtra(ExtraConstants.EXTRA_ROLL, roll);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_INSERT: {
                Roll roll = intent.getParcelableExtra(ExtraConstants.EXTRA_ROLL);
                ManagerFactory.getInstance(this).getEntityManager(Roll.class).insert(roll);
                Intent res = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_EDIT: {
                Roll roll = intent.getParcelableExtra(ExtraConstants.EXTRA_ROLL);
                ManagerFactory.getInstance(this).getEntityManager(Roll.class).update(roll);
                Intent res = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_DELETE: {
                Intent res = new Intent(action);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                if (id == -1)
                    break;
                boolean result = ManagerFactory.getInstance(this).getEntityManager(Roll.class).delete(id);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_GET_BY_FRAME_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                long frameId = intent.getLongExtra(ExtraConstants.EXTRA_FRAME_ID, -1);
                Intent res = new Intent(action);
                List<Roll> rolls = ((IRollManager)ManagerFactory.getInstance(this).getEntityManager(Roll.class)).getByFrameId(matchId, frameId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_ROLLS, new ArrayList<>(rolls));
                break;
            }
        }
    }
}
