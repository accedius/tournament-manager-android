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
import fit.cvut.org.cz.squash.business.entities.communication.Constants;
import fit.cvut.org.cz.squash.business.managers.StatisticManager;
import fit.cvut.org.cz.squash.presentation.adapters.StandingsAdapter;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Allows user to display tournament standings
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingsListFragment extends AbstractListFragment<StandingItem> {
    private String orderColumn = Constants.POINTS;
    private String orderType = Constants.ORDER_DESC;

    public static StandingsListFragment newInstance(long id, CompetitionType type) {
        StandingsListFragment fragment = new StandingsListFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putParcelable(ExtraConstants.EXTRA_TYPE, type);
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
        if (orderColumn.equals(stat) && orderType.equals(Constants.ORDER_DESC)) {
            orderType = Constants.ORDER_ASC;
            Collections.sort(stats, new Comparator<StandingItem>() {
                @Override
                public int compare(StandingItem ls, StandingItem rs) {
                    return (int) (100D*ls.getStat(stat) - 100D*rs.getStat(stat));
                }
            });
        } else {
            if (!orderColumn.equals(stat)) {
                orderColumn = stat;
            }
            orderType = Constants.ORDER_DESC;
            if (orderColumn.equals(Constants.POINTS)) {
                StatisticManager.orderStandings(stats);
            } else {
                Collections.sort(stats, new Comparator<StandingItem>() {
                    @Override
                    public int compare(StandingItem ls, StandingItem rs) {
                        return (int) (100D*rs.getStat(stat) - 100D*ls.getStat(stat));
                    }
                });
            }
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
        return new StandingsAdapter(getActivity(), (CompetitionType)getArguments().getParcelable(ExtraConstants.EXTRA_TYPE));
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_STATS;
    }

    @Override
    public void askForData() {
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_STANDINGS, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
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
