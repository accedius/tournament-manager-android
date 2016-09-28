package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.adapters.AggregatedStatsAdapter;
import fit.cvut.org.cz.squash.presentation.dialogs.AggregatedStatsDialog;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.listeners.PlayerDetailOnClickListener;

/**
 * Displays list of aggregated statistics for tournament or competition
 * Created by Vaclav on 5. 4. 2016.
 */
public class AggregatedStatsListFragment extends AbstractListFragment<SAggregatedStats> {
    public static final String ARG_ID = "ARG_ID";
    public static final String ARG_ACTION = "ARG_ACTION";
    public static final String SAVE_MAIN = "SAVE_MAIN";
    public static final String SAVE_ADD = "SAVE_ADD";
    public static final String SAVE_SEND = "SAVE_SEND";
    public static final String SAVE_DELETE = "SAVE_DELETE";
    public static final int REQUEST_PLAYERS_FOR_COMPETITION = 1;
    public static final int REQUEST_PLAYERS_FOR_TOURNAMENT = 2;

    private String mainAction = null;
    private String addAction = null;
    private String deleteAction = null;
    private int requestCode = 0;
    private boolean sendForData = true;
    private AggregatedStatsAdapter adapter = null;
    private String orderColumn = "p";
    private String orderType = "DESC";

    private BroadcastReceiver refreshReceiver = new RefreshReceiver();

    public static AggregatedStatsListFragment newInstance(long id, String action){
        AggregatedStatsListFragment fragment = new AggregatedStatsListFragment();
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
        outState.putString(SAVE_DELETE, deleteAction);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainAction = getArguments().getString(ARG_ACTION, null);

        if (savedInstanceState != null) {
            mainAction = savedInstanceState.getString(SAVE_MAIN);
            addAction = savedInstanceState.getString(SAVE_ADD);
            sendForData = savedInstanceState.getBoolean(SAVE_SEND);
            deleteAction = savedInstanceState.getString(SAVE_DELETE);
        }

        switch (mainAction){
            case StatsService.ACTION_GET_STATS_BY_COMPETITION:
                requestCode = 1;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION;
                deleteAction = PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION;
                break;
            case StatsService.ACTION_GET_STATS_BY_TOURNAMENT:
                requestCode = 2;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT;
                deleteAction = PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT;
                break;
            default: break;
        }
    }

    public void orderData(final String stat, HashMap<String, TextView> columns) {
        if (adapter == null) return;

        TextView col = columns.get(orderColumn);
        String text = (String) col.getText();
        String originalText = text.substring(0, text.length()-2);
        col.setText(originalText);

        ArrayList<SAggregatedStats> stats = adapter.getData();
        if (orderColumn.equals(stat) && orderType == "DESC") {
            orderType = "ASC";
            Collections.sort(stats, new Comparator<SAggregatedStats>() {
                @Override
                public int compare(SAggregatedStats ls, SAggregatedStats rs) {
                    return (int) (ls.getStat(stat) - rs.getStat(stat));
                }
            });
        } else {
            if (!orderColumn.equals(stat)) {
                orderColumn = stat;
            }
            orderType = "DESC";
            Collections.sort(stats, new Comparator<SAggregatedStats>() {
                @Override
                public int compare(SAggregatedStats ls, SAggregatedStats rs) {
                    return (int) (rs.getStat(stat) - ls.getStat(stat));
                }
            });
        }

        col = columns.get(stat);
        text = (String) col.getText();
        String addition = "";
        if (orderType.equals("ASC")) {
            addition = "▲";
        } else if (orderType.equals("DESC")) {
            addition = "▼";
        }
        col.setText(text + " " + addition);

        adapter.swapData(stats);
        adapter.notifyDataSetChanged();
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
        adapter = new AggregatedStatsAdapter() {
            @Override
            protected void setOnClickListeners(View v, final SAggregatedStats item, final int position, final String name) {
                v.setOnClickListener(PlayerDetailOnClickListener.getListener(getContext(), item.playerId));
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final long id = getArguments().getLong(ARG_ID);
                        AggregatedStatsDialog dialog = AggregatedStatsDialog.newInstance(id, item.playerId, position, deleteAction, name);
                        dialog.show(getFragmentManager(), "DELETE_DD");
                        return false;
                    }
                });
            }
        };
        return adapter;
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STATS;
    }

    @Override
    public void askForData() {
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
        filter.addAction(deleteAction);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != SelectableListActivity.RESULT_OK) {
            sendForData = true;
            askForData();
            return;
        }
        if (addAction == null)
            return;

        if (requestCode == 3) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            return;
        }

        Intent intent = PlayerService.newStartIntent(addAction, getContext());
        intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA));
        intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
        progressBar.setVisibility(View.VISIBLE);
    }

    public class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action){
                case PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION:
                case PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT:
                    sendForData = true;
                    askForData();
                    progressBar.setVisibility(View.VISIBLE);
                    contentView.setVisibility(View.GONE);
                    break;
                case StatsService.ACTION_GET_STATS_BY_COMPETITION:
                case StatsService.ACTION_GET_STATS_BY_TOURNAMENT:
                    AggregatedStatsListFragment.super.bindDataOnView(intent);
                    break;
                case PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION:{
                    if (intent.getBooleanExtra(PlayerService.EXTRA_RESULT, false)) {
                        int position = intent.getIntExtra(PlayerService.EXTRA_POSITION, -1);
                        adapter.delete(position);
                    }
                    else Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.failDeletePlayerFromCompetition, Snackbar.LENGTH_LONG).show();
                    break;
                }
                case PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT:{
                    if (intent.getBooleanExtra(PlayerService.EXTRA_RESULT, false)) {
                        int position = intent.getIntExtra(PlayerService.EXTRA_POSITION, -1);
                        adapter.delete(position);
                    }
                    else Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.failDeletePlayerFromTournament, Snackbar.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }

}
