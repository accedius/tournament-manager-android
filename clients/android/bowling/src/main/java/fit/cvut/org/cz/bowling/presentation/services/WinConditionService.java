package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

public class WinConditionService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_WIN_CONDITION_OF_TOURNAMENT = "action_get_win_condition_of_tournament";

    public WinConditionService() {
        super("Bowling win condition Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, WinConditionService.class);
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
            case ACTION_GET_WIN_CONDITION_OF_TOURNAMENT: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
                WinCondition winCondition = ((IWinConditionManager) ManagerFactory.getInstance(this).getEntityManager(WinCondition.class)).getByTournamentId(id);
                Intent res = new Intent(ACTION_GET_WIN_CONDITION_OF_TOURNAMENT);
                res.putExtra(ExtraConstants.EXTRA_WIN_CONDITION, winCondition);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
