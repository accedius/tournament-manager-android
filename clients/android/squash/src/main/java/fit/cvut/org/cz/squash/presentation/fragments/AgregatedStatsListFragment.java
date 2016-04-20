package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    public static final String ARG_ACTION = "ARG_ACTION";
    public static final String SAVE_MAIN = "SAVE_MAIN";
    public static final String SAVE_ADD = "SAVE_ADD";
    public static final String SAVE_SEND = "SAVE_SEND";
    public static final int REQUEST_PLAYERS_FOR_COMPETITION = 1;
    public static final int REQUEST_PLAYERS_FOR_TOURNAMENT = 2;

    private String mainAction = null;
    private String addAction = null;
    private int requestCode = 0;
    private boolean sendForData = true;

    private BroadcastReceiver refreshReceiver = new RefreshReceiver();

    public static AgregatedStatsListFragment newInstance(long id, String action){

        AgregatedStatsListFragment fragment = new AgregatedStatsListFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        b.putString(ARG_ACTION, action);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SEND, sendForData);
        outState.putString(SAVE_MAIN, mainAction);
        outState.putString(SAVE_ADD, addAction);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainAction = getArguments().getString(ARG_ACTION, null);

        if (savedInstanceState != null){
            mainAction = savedInstanceState.getString(SAVE_MAIN);
            addAction = savedInstanceState.getString(SAVE_ADD);
            sendForData = savedInstanceState.getBoolean(SAVE_SEND);
        }

        switch (mainAction){
            case StatsService.ACTION_GET_STATS_BY_COMPETITION:
                requestCode = 1;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION;
                break;
            case StatsService.ACTION_GET_STATS_BY_TOURNAMENT:
                requestCode = 2;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT;
                break;
            default: break;
        }
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (isDataSourceWorking()) return;
                sendForData = false;
                switch (requestCode){
                    case REQUEST_PLAYERS_FOR_COMPETITION:
                        intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_COMPETITION, getArguments().getLong(ARG_ID));
                        break;
                    case REQUEST_PLAYERS_FOR_TOURNAMENT:
                        intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TOURNAMENT, getArguments().getLong(ARG_ID));
                        break;
                    default:break;
                }
                if (intent != null)
                    startActivityForResult(intent, requestCode);
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

        if (mainAction != null && sendForData) {
            Intent intent = StatsService.newStartIntent(mainAction, getContext());
            intent.putExtra(StatsService.EXTRA_ID, getArguments().getLong(ARG_ID));

            getContext().startService(intent);
        }
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(mainAction);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(mainAction);
        filter.addAction(addAction);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != SelectableListActivity.RESULT_OK){
            sendForData = true;
            askForData();
            return;
        }
        if (addAction == null) return;

        Intent intent = PlayerService.newStartIntent(addAction, getContext());
        intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA));
        intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
        progressBar.setVisibility(View.VISIBLE);

    }

    public class RefreshReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action){
                case PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION:
                case PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT:
                    sendForData = true;
                    askForData();
                    break;
                case StatsService.ACTION_GET_STATS_BY_COMPETITION:
                case StatsService.ACTION_GET_STATS_BY_TOURNAMENT:
                    AgregatedStatsListFragment.super.bindDataOnView(intent);
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    break;
                default:break;
            }

        }
    }
}
