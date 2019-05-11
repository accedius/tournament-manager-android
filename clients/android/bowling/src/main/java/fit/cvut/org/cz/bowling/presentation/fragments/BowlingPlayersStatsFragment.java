package fit.cvut.org.cz.bowling.presentation.fragments;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.bowling.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.AggregatedStatisticsAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.DeleteOnlyDialog;
import fit.cvut.org.cz.bowling.presentation.services.PlayerService;
import fit.cvut.org.cz.bowling.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.listeners.PlayerDetailOnClickListener;

public class BowlingPlayersStatsFragment extends AbstractListFragment<AggregatedStatistics> {
    private long competitionID;
    private long tournamentID;
    public static final String SAVE_COMP_ID = "SAVE_COMP_ID";
    public static final String SAVE_TOUR_ID = "SAVE_TOUR_ID";
    public static final String SAVE_SEND = "SAVE_SEND";

    private boolean sendForData = true;
    private String orderColumn = Constants.POINTS;
    private String orderType = Constants.ORDER_DESC;

    private BroadcastReceiver statsReceiver = new StatsReceiver();

    public static BowlingPlayersStatsFragment newInstance(long id, boolean forComp) {
        BowlingPlayersStatsFragment fragment = new BowlingPlayersStatsFragment();
        Bundle args = new Bundle();

        if (forComp)
            args.putLong(ExtraConstants.EXTRA_COMP_ID, id);
        else
            args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SEND, sendForData);
        outState.putLong(SAVE_COMP_ID, competitionID);
        outState.putLong(SAVE_TOUR_ID, tournamentID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            tournamentID = savedInstanceState.getLong(SAVE_TOUR_ID);
            competitionID = savedInstanceState.getLong(SAVE_COMP_ID);
            sendForData = savedInstanceState.getBoolean(SAVE_SEND);
        }

        if (getArguments() != null) {
            competitionID = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
            tournamentID = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);
        }
    }

    public void orderData(final String stat, HashMap<String, TextView> columns) {
        if (adapter == null) return;

        TextView col = columns.get(orderColumn);
        String text = (String) col.getText();
        if (text.contains(Constants.DESC_SIGN) || text.contains(Constants.ASC_SIGN)) {
            String originalText = text.substring(0, text.length() - 2);
            col.setText(originalText);
        }

        List<AggregatedStatistics> stats = adapter.getData();
        if (orderColumn.equals(stat) && orderType.equals(Constants.ORDER_DESC)) {
            orderType = Constants.ORDER_ASC;
            Collections.sort(stats, new Comparator<AggregatedStatistics>() {
                @Override
                public int compare(AggregatedStatistics ls, AggregatedStatistics rs) {
                    return (int) (100D*ls.getStat(stat) - 100D*rs.getStat(stat));
                }
            });
        } else {
            if (!orderColumn.equals(stat)) {
                orderColumn = stat;
            }
            orderType = Constants.ORDER_DESC;
            Collections.sort(stats, new Comparator<AggregatedStatistics>() {
                @Override
                public int compare(AggregatedStatistics ls, AggregatedStatistics rs) {
                    return (int) (100D*rs.getStat(stat) - 100D*ls.getStat(stat));
                }
            });
        }

        col = columns.get(stat);
        text = (String) col.getText();
        String addition = "";
        if (orderType.equals(Constants.ORDER_ASC)) {
            addition = Constants.ASC_SIGN;
        } else if (orderType.equals(Constants.ORDER_DESC)) {
            addition = Constants.DESC_SIGN;
        }
        col.setText(text + " " + addition);

        adapter.swapData(stats);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new AggregatedStatisticsAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long playerId, final String name) {
                super.setOnClickListeners(v, playerId, name);
                v.setOnClickListener(PlayerDetailOnClickListener.getListener(getContext(), playerId));
                v.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        DeleteOnlyDialog dialog = DeleteOnlyDialog.newInstance(playerId, competitionID, tournamentID, name);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");
                        return true;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_STATS;
    }

    @Override
    public void askForData() {
        if (!sendForData) return;

        Intent intent;
        if (competitionID != -1) {
            intent = StatsService.newStartIntent(StatsService.ACTION_GET_BY_COMP_ID, getContext());
            intent.putExtra(ExtraConstants.EXTRA_ID, competitionID);
        } else {
            intent = StatsService.newStartIntent(StatsService.ACTION_GET_BY_TOUR_ID, getContext());
            intent.putExtra(ExtraConstants.EXTRA_ID, tournamentID);
        }

        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        if (competitionID != -1) {
            boolean answer = StatsService.isWorking(StatsService.ACTION_GET_BY_COMP_ID);
            return answer;
        } else {
            return StatsService.isWorking(StatsService.ACTION_GET_BY_TOUR_ID);
        }
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter;

        if (competitionID != -1) {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_COMP_ID);
            filter.addAction(PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION);
            filter.addAction(PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION);
        } else {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_TOUR_ID);
            filter.addAction(PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT);
            filter.addAction(PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT);
        }

        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(statsReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(statsReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        final LayoutInflater layourInflater = LayoutInflater.from(getContext());
        FloatingActionButton fab = (FloatingActionButton) layourInflater.inflate(R.layout.floatingbutton_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       int requestCode;
                                       int option;
                                       long id;
                                       sendForData = false;
                                       if (competitionID != -1) {
                                           option = AddPlayersFragment.OPTION_COMPETITION;
                                           id = competitionID;
                                           requestCode = option;
                                       } else {
                                           option = AddPlayersFragment.OPTION_TOURNAMENT;
                                           id = tournamentID;
                                           requestCode = option;
                                       }

                                       Intent intent = AddPlayersActivity.newStartIntent(getContext(), option, id);
                                       startActivityForResult(intent, requestCode);
                                   }
                               }
        );
        return fab;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != SelectableListActivity.RESULT_OK) {
            sendForData = true;
            askForData();
            return;
        }

        if (competitionID != -1) {
            Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION, getContext());
            intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA));
            intent.putExtra(ExtraConstants.EXTRA_ID, competitionID);
            getContext().startService(intent);
        } else {
            Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT, getContext());
            intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA));
            intent.putExtra(ExtraConstants.EXTRA_ID, tournamentID);
            getContext().startService(intent);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    public class StatsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case StatsService.ACTION_GET_BY_TOUR_ID:
                case StatsService.ACTION_GET_BY_COMP_ID: {
                    BowlingPlayersStatsFragment.super.bindDataOnView(intent);
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT:
                case PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION: {
                    sendForData = true;
                    askForData();
                    break;
                }
                case PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION:
                    if (intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                        sendForData = true;
                        askForData();
                        break;
                    } else {
                        View v = getView();
                        if (v != null) Snackbar.make(v, R.string.player_delete_from_competition_error, Snackbar.LENGTH_LONG).show();
                    }
                case PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT: {
                    if (intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                        sendForData = true;
                        askForData();
                        break;
                    } else {
                        View v = getView();
                        if (v != null) Snackbar.make(v, R.string.player_delete_from_tournament_error, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

}
