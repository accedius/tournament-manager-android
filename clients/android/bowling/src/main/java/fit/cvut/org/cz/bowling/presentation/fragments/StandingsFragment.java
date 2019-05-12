package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.bowling.business.entities.Standing;
import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.bowling.business.managers.StatisticManager;
import fit.cvut.org.cz.bowling.presentation.adapters.StandingsAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment for standings in tournament
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsFragment extends AbstractListFragment<Standing> {
    private String orderColumn = Constants.POINTS;
    private String orderType = Constants.ORDER_DESC;

    public static StandingsFragment newInstance(long id) {
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public void orderData(final String stat, HashMap<String, TextView> columns) {
        if (adapter == null) return;

        TextView col = columns.get(orderColumn);
        String text = (String) col.getText();
        String originalText = text.substring(0, text.length()-2);
        col.setText(originalText);

        List<Standing> stats = adapter.getData();
        if (orderColumn.equals(stat) && orderType.equals(Constants.ORDER_DESC)) {
            orderType = Constants.ORDER_ASC;
            Collections.sort(stats, new Comparator<Standing>() {
                @Override
                public int compare(Standing ls, Standing rs) {
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
                Collections.sort(stats, new Comparator<Standing>() {
                    @Override
                    public int compare(Standing ls, Standing rs) {
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
        return new StandingsAdapter();
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_STANDINGS;
    }

    @Override
    public void askForData() {
        Long tournamentID = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, tournamentID);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
