package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.presentation.adapters.StandingsAdapter;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Allows user to display tournament standings
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingsListFragment extends AbstractListFragment<StandingItem> {
    public static final String ARG_ID = "arg_id";
    public static final String ARG_TYPE = "arg_type";

    private String orderColumn = "p";
    private String orderType = "DESC";

    public static StandingsListFragment newInstance(long id, CompetitionType type) {
        StandingsListFragment fragment = new StandingsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putParcelable(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public void orderData(final String stat, HashMap<String, TextView> columns) {
        if (adapter == null) return;

        TextView col = columns.get(orderColumn);
        String text = (String) col.getText();
        String originalText = text.substring(0, text.length()-2);
        col.setText(originalText);

        List<StandingItem> stats = adapter.getData();
        if (orderColumn.equals(stat) && orderType == "DESC") {
            orderType = "ASC";
            Collections.sort(stats, new Comparator<StandingItem>() {
                @Override
                public int compare(StandingItem ls, StandingItem rs) {
                    return (int) (ls.getStat(stat) - rs.getStat(stat));
                }
            });
        } else {
            if (!orderColumn.equals(stat)) {
                orderColumn = stat;
            }
            orderType = "DESC";
            // TODO better this sorting
            if (orderColumn == "p") {
                Collections.sort(stats, new Comparator<StandingItem>() {
                    @Override
                    public int compare(StandingItem lhs, StandingItem rhs) {
                        if (rhs.points != lhs.points)
                            return rhs.points - lhs.points;
                        if ((rhs.setsWon - rhs.setsLost) != (lhs.setsWon - lhs.setsLost))
                            return (rhs.setsWon - rhs.setsLost - lhs.setsWon + lhs.setsLost);
                        return rhs.setsWon - lhs.setsWon;
                    }
                });
            } else {
                Collections.sort(stats, new Comparator<StandingItem>() {
                    @Override
                    public int compare(StandingItem ls, StandingItem rs) {
                        return (int) (rs.getStat(stat) - ls.getStat(stat));
                    }
                });
            }
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
    protected AbstractListAdapter getAdapter() {
        return new StandingsAdapter(getActivity(), (CompetitionType)getArguments().getParcelable(ARG_TYPE));
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STATS;
    }

    @Override
    public void askForData() {
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_STANDINGS, getContext());
        intent.putExtra(StatsService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_STANDINGS);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(StatsService.ACTION_GET_STANDINGS));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
