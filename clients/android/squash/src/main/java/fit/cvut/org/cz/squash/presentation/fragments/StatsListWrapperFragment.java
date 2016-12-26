package fit.cvut.org.cz.squash.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Header for aggregated stats list
 * Created by Vaclav on 10. 4. 2016.
 */
public class StatsListWrapperFragment extends Fragment {
    public static final String ARG_ID = "arg_id";
    public static final String ARG_ACTION = "arg_action";

    private AggregatedStatsListFragment statsFragment;

    public static StatsListWrapperFragment newInstance(long id, String action){
        StatsListWrapperFragment fragment = new StatsListWrapperFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_ACTION, action);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_stats_wrapper, container, false);
        setOrderingListeners(v);
        setDefaultOrder(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getChildFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            statsFragment = AggregatedStatsListFragment.newInstance(getArguments().getLong(ARG_ID), getArguments().getString(ARG_ACTION));
            getChildFragmentManager().beginTransaction().add(R.id.fragment_container, statsFragment).commit();
        }
    }

    /**
     * Bridge for refreshing
     */
    public void refresh() {
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.fragment_container);
        if (fr != null && fr instanceof AbstractDataFragment) {
            ((AbstractDataFragment) fr).customOnResume();
            setDefaultOrder(getView());
        }
    }

    private void setDefaultOrder(View v) {
        deleteOtherOrders(v);
        TextView points = (TextView)v.findViewById(R.id.tv_points_label);
        points.setText(points.getText()+ " ▼");
    }

    private void deleteOtherOrders(View v) {
        final HashMap<String, TextView> columns = getColumns(v);
        for (TextView textView : columns.values()) {
            if (textView.getText().toString().contains("▼") || textView.getText().toString().contains("▲")) {
                String text = textView.getText().toString();
                textView.setText(text.substring(0, text.length()-2));
            }
        }
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = getColumns(v);
        for (final Map.Entry<String, TextView> e : columns.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statsFragment.orderData(e.getKey(), columns);
                }
            });
        }
    }

    private HashMap<String, TextView> getColumns(View v) {
        HashMap<String, TextView> columns = new HashMap<>();
        columns.put("gp",(TextView)v.findViewById(R.id.tv_games_played_label));
        columns.put("p", (TextView)v.findViewById(R.id.tv_points_label));
        columns.put("w", (TextView)v.findViewById(R.id.tv_wins_label));
        columns.put("l", (TextView)v.findViewById(R.id.tv_losses_label));
        columns.put("d", (TextView)v.findViewById(R.id.tv_draws_label));
        if (v.findViewById(R.id.tv_won_per_label) != null) {
            columns.put("w%", (TextView)v.findViewById(R.id.tv_won_per_label));
            columns.put("sw", (TextView) v.findViewById(R.id.tv_sets_won_label));
            columns.put("sl", (TextView) v.findViewById(R.id.tv_sets_lost_label));
            columns.put("swavg", (TextView) v.findViewById(R.id.tv_sets_won_avg_label));
            columns.put("slavg", (TextView) v.findViewById(R.id.tv_sets_lost_avg_label));
            columns.put("s%", (TextView) v.findViewById(R.id.tv_sets_per_label));
            columns.put("bw", (TextView) v.findViewById(R.id.tv_balls_won_label));
            columns.put("bl", (TextView) v.findViewById(R.id.tv_balls_lost_label));
            columns.put("bwavg", (TextView) v.findViewById(R.id.tv_balls_won_avg_label));
            columns.put("blavg", (TextView) v.findViewById(R.id.tv_balls_lost_avg_label));
        }
        return columns;
    }
}
