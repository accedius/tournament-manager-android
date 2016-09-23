package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.presentation.adapters.StandingsAdapter;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment for standings in tournament
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsFragment extends AbstractListFragment<Standing> {

    private static String ARG_ID = "tournament_id";

    private String orderColumn = "p";
    private String orderType = "DESC";

    public static StandingsFragment newInstance( long id ) {
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public void orderData(final String stat, HashMap<String, TextView> columns) {
        if (adapter == null) return;

        TextView col = columns.get(orderColumn);
        String text = (String) col.getText();
        String originalText = text.substring(0, text.length()-2);
        col.setText(originalText);

        ArrayList<Standing> stats = adapter.getData();
        if (orderColumn.equals(stat) && orderType == "DESC") {
            orderType = "ASC";
            Collections.sort(stats, new Comparator<Standing>() {
                @Override
                public int compare(Standing ls, Standing rs) {
                    return (int) (ls.getStat(stat) - rs.getStat(stat));
                }
            });
        } else {
            if (!orderColumn.equals(stat)) {
                orderColumn = stat;
            }
            orderType = "DESC";
            // TODO order for points should be the same order as defined in HOCKEYHOCKEY RULES - Points, less matches, wins, score, ...
            Collections.sort(stats, new Comparator<Standing>() {
                @Override
                public int compare(Standing ls, Standing rs) {
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
    protected AbstractListAdapter getAdapter() {
        return new StandingsAdapter();
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STANDINGS;
    }

    @Override
    public void askForData() {
        Long tournamentID = getArguments().getLong(ARG_ID, -1);
        Intent intent = StatsService.newStartIntent( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT, getContext() );
        intent.putExtra( StatsService.EXTRA_ID, tournamentID );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT ));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
