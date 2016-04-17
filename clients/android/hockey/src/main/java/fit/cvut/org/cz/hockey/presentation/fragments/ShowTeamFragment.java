package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class ShowTeamFragment extends TeamDetailFragment {



    @Override
    protected String getTeamKey() {
        return TeamService.EXTRA_TEAM;
    }

    @Override
    protected String getExtraPlayersKey() {
        return SelectableListActivity.EXTRA_DATA;
    }

    @Override
    protected void updatePlayers(Team t) {
        //TODO
    }

    @Override
    protected Intent getSelectActivityStartIntent(Team t) {
        //TODO
        return null;
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
    protected void askForData() {
        Intent intent = TeamService.newStartIntent(TeamService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(TeamService.EXTRA_ID, teamId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking( TeamService.ACTION_GET_BY_ID );
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
