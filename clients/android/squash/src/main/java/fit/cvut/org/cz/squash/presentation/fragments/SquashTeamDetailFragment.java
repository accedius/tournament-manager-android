package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**Allows user to display and manage team
 * Created by Vaclav on 15. 4. 2016.
 */
public class SquashTeamDetailFragment extends TeamDetailFragment {
    @Override
    protected String getTeamKey() {
        return ExtraConstants.EXTRA_TEAM;
    }

    @Override
    protected String getExtraPlayersKey() {
        return ExtraConstants.EXTRA_DATA;
    }

    @Override
    protected void updatePlayers(Team t) {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_UPDATE_PLAYERS_IN_TEAM, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, t.getId());
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(t.getPlayers()));
        getContext().startService(intent);
    }

    @Override
    protected Intent getSelectActivityStartIntent(Team t) {
        Intent intent =  AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TEAM, t.getId());
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, new ArrayList<>(t.getPlayers()));
        return intent;
    }

    @Override
    protected int getRequestCode() {
        return 0;
    }

    @Override
    protected int getOKResultCode() {
        return SelectableListActivity.RESULT_OK;
    }

    @Override
    public void askForData() {
        Intent intent = TeamService.newStartIntent(TeamService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, teamId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking(TeamService.ACTION_GET_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TeamService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
