package fit.cvut.org.cz.squash.presentation.dialogs;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/** Dialog for creating or editing team
 * Created by Vaclav on 14. 4. 2016.
 */
public class SquashInsertTeamDialog extends InsertTeamDialog {
    @Override
    protected void askForData() {
        Intent i = TeamService.newStartIntent(TeamService.ACTION_GET_BY_ID, getContext());
        i.putExtra(ExtraConstants.EXTRA_ID, teamId);
        getContext().startService(i);
    }

    @Override
    protected void registerReceiver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TeamService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceiver() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected void insertTeam(Team t) {
        Intent i = TeamService.newStartIntent(TeamService.ACTION_ADD_TEAM, getContext());
        i.putExtra(ExtraConstants.EXTRA_TEAM, t);
        getContext().startService(i);
    }

    @Override
    protected void editTeam(Team t) {
        Intent i = TeamService.newStartIntent(TeamService.ACTION_EDIT_TEAM, getContext());
        i.putExtra(ExtraConstants.EXTRA_TEAM, t);
        getContext().startService(i);
    }

    @Override
    protected String getTeamKey() {
        return ExtraConstants.EXTRA_TEAM;
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking(TeamService.ACTION_GET_BY_ID);
    }
}
