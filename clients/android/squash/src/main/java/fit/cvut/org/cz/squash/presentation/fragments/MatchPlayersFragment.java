package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchPlayersFragment extends TeamDetailFragment {

    private static final String ARG_ID = "arg_id";
    private static final String ARG_ACTION = "arg_action";

    public static MatchPlayersFragment newInstance(long id, String action){
        MatchPlayersFragment f = new MatchPlayersFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        b.putString(ARG_ACTION, action);
        f.setArguments(b);
        return f;
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = super.injectView(inflater, container);
        TextView teamName = (TextView) v. findViewById(fit.cvut.org.cz.tmlibrary.R.id.tv_name);
        teamName.setVisibility(View.GONE);
        return v;
    }

    @Override
    protected void askForData() {
        Intent intent = PlayerService.newStartIntent(getArguments().getString(ARG_ACTION), getContext());
        intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
    }

    @Override
    protected Intent getSelectActivityStartIntent(Team t) {
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_MATCH, t.getId());
        intent.putParcelableArrayListExtra(AddPlayersActivity.EXTRA_OMIT_DATA, t.getPlayers());

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
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(getArguments().getString(ARG_ACTION)));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(getArguments().getString(ARG_ACTION));
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (t == null) t = new Team();
        ArrayList<Player> players = intent.getParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS);
        t.setPlayers(players);
        t.setId(intent.getLongExtra(PlayerService.EXTRA_ID, -1));
        adapter.swapData(players);
        sendForData = false;
        unregisterReceivers();
    }

    @Override
    protected String getTeamKey() {
        return null;
    }

    @Override
    protected String getExtraPlayersKey() {
        return AddPlayersActivity.EXTRA_DATA;
    }

    @Override
    protected void updatePlayers(Team t) {}

    public ArrayList<Player> getPlayers() {return adapter.getData();}

    @Override
    protected void customOnResume() {
        if (t != null && t.getPlayers() != null) adapter.swapData(t.getPlayers());
        if (sendForData){
            registerReceivers();
            if (!isWorking())
                askForData();
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
