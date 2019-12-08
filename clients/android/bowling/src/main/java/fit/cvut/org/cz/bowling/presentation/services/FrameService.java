package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

public class FrameService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_ID = "action_get_by_id";
    public static final String ACTION_INSERT = "action_insert_frame";
    public static final String ACTION_EDIT = "action_edit_frame";
    public static final String ACTION_DELETE = "action_delete_frame";
    public static final String ACTION_GET_BY_MATCH_ID = "action_get_by_match_id";
    public static final String ACTION_GET_IN_MATCH_BY_PARTICIPANT_ID = "action_get_in_match_by_participant_id";
    public static final String ACTION_GET_IN_MATCH_BY_PLAYER_ID = "action_get_in_match_by_player_id";
    public static final String ACTION_DELETE_ALL_BY_MATCH_ID = "action_delete_all_by_match_id";

    public FrameService() {
        super("Bowling Frame Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, FrameService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_ID: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Frame fr = ManagerFactory.getInstance(this).getEntityManager(Frame.class).getById(id);
                Intent res = new Intent(ACTION_GET_BY_ID);
                res.putExtra(ExtraConstants.EXTRA_FRAME, fr);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_INSERT: {
                Frame fr = intent.getParcelableExtra(ExtraConstants.EXTRA_FRAME);
                ManagerFactory.getInstance(this).getEntityManager(Frame.class).insert(fr);
                Intent res = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_EDIT: {
                Frame fr = intent.getParcelableExtra(ExtraConstants.EXTRA_FRAME);
                ManagerFactory.getInstance(this).getEntityManager(Frame.class).update(fr);
                Intent res = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_DELETE: {
                Intent res = new Intent(action);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                if (id == -1)
                    break;
                boolean result = ManagerFactory.getInstance(this).getEntityManager(Frame.class).delete(id);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_GET_BY_MATCH_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                Intent res = new Intent(action);
                List<Frame> frames = ((IFrameManager)ManagerFactory.getInstance(this).getEntityManager(Frame.class)).getByMatchId(matchId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_FRAMES, new ArrayList<>(frames));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_GET_IN_MATCH_BY_PARTICIPANT_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                long participantId = intent.getLongExtra(ExtraConstants.EXTRA_PARTICIPANT_ID, -1);
                Intent res = new Intent(action);
                List<Frame> frames = ((IFrameManager)ManagerFactory.getInstance(this).getEntityManager(Frame.class)).getInMatchByParticipantId(participantId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_FRAMES, new ArrayList<>(frames));
                break;
            }

            case ACTION_GET_IN_MATCH_BY_PLAYER_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                long playerId = intent.getLongExtra(ExtraConstants.EXTRA_PLAYER_ID, -1);
                Intent res = new Intent(action);
                List<Frame> frames = ((IFrameManager)ManagerFactory.getInstance(this).getEntityManager(Frame.class)).getInMatchByPlayerId(matchId, playerId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_FRAMES, new ArrayList<>(frames));
                break;
            }

            case ACTION_DELETE_ALL_BY_MATCH_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                if (matchId == -1)
                    break;
                boolean result = ((IFrameManager)ManagerFactory.getInstance(this).getEntityManager(Frame.class)).deleteAllByMatchId(matchId);
                Intent res = new Intent(action);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
