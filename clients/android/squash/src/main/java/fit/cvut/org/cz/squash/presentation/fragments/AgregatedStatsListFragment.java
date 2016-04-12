package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.adapters.AgregatedStatsAdapter;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class AgregatedStatsListFragment extends AbstractListFragment<AgregatedStats> {

    public static final String ARG_ID = "ARG_ID";
    public static final int REQUEST_PLAYERS_FOR_COMPETITION = 1;


    public static AgregatedStatsListFragment newInstance(long competitionId){
        AgregatedStatsListFragment fragment = new AgregatedStatsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, competitionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_COMPETITION, getArguments().getLong(ARG_ID));
                startActivityForResult(intent, REQUEST_PLAYERS_FOR_COMPETITION);
            }
        });

        return fab;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new AgregatedStatsAdapter();
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STATS;
    }

    @Override
    protected void askForData() {
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_STATS_BY_COMPETITION, getContext());
        intent.putExtra(StatsService.EXTRA_ID, getArguments().getLong(ARG_ID));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_STATS_BY_COMPETITION);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(StatsService.ACTION_GET_STATS_BY_COMPETITION));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != SelectableListActivity.RESULT_OK) return;
        switch (requestCode){
            case REQUEST_PLAYERS_FOR_COMPETITION: {
                Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION, getContext());
                intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA));
                getContext().startService(intent);
                progressInterface.showProgress();
                break;
            }
            default:break;
        }

    }
}
