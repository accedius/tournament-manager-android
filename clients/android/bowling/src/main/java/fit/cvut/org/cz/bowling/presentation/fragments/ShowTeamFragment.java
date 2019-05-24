package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PlayerService;
import fit.cvut.org.cz.bowling.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

public class ShowTeamFragment extends TeamDetailFragment {
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
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_UPDATE_TEAM_PLAYERS, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, t.getId());
        intent.putExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(t.getPlayers()));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_cancel, menu);
        super.onSkipCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == fit.cvut.org.cz.tmlibrary.R.id.action_finish || itemId == android.R.id.home) {
            t.setPlayers(adapter.getData());
            updatePlayers(t);
            getActivity().finish();
            return true;
        } else if (itemId == fit.cvut.org.cz.tmlibrary.R.id.action_cancel) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
