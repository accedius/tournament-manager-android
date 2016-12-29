package fit.cvut.org.cz.squash.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.communication.Constants;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Header for aggregated stats list
 * Created by Vaclav on 10. 4. 2016.
 */
public class StatsListWrapperFragment extends Fragment {
    private AggregatedStatsListFragment statsFragment;

    public static StatsListWrapperFragment newInstance(long id, String action){
        StatsListWrapperFragment fragment = new StatsListWrapperFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putString(ExtraConstants.EXTRA_ACTION, action);

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
            statsFragment = AggregatedStatsListFragment.newInstance(getArguments().getLong(ExtraConstants.EXTRA_ID), getArguments().getString(ExtraConstants.EXTRA_ACTION));
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
        points.setText(points.getText()+ " " + Constants.DESC_SIGN);
    }

    private void deleteOtherOrders(View v) {
        final HashMap<String, TextView> columns = getColumns(v);
        for (TextView textView : columns.values()) {
            if (textView.getText().toString().contains(Constants.DESC_SIGN) || textView.getText().toString().contains(Constants.ASC_SIGN)) {
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
        columns.put(Constants.MATCHES,(TextView)v.findViewById(R.id.tv_games_played_label));
        columns.put(Constants.POINTS, (TextView)v.findViewById(R.id.tv_points_label));
        columns.put(Constants.WINS, (TextView)v.findViewById(R.id.tv_wins_label));
        columns.put(Constants.LOSSES, (TextView)v.findViewById(R.id.tv_losses_label));
        columns.put(Constants.DRAWS, (TextView)v.findViewById(R.id.tv_draws_label));
        if (v.findViewById(R.id.tv_won_per_label) != null) {
            columns.put(Constants.MATCH_WIN_RATE, (TextView)v.findViewById(R.id.tv_won_per_label));
            columns.put(Constants.SETS_WON, (TextView) v.findViewById(R.id.tv_sets_won_label));
            columns.put(Constants.SETS_LOST, (TextView) v.findViewById(R.id.tv_sets_lost_label));
            columns.put(Constants.SETS_WON_AVG, (TextView) v.findViewById(R.id.tv_sets_won_avg_label));
            columns.put(Constants.SETS_LOST_AVG, (TextView) v.findViewById(R.id.tv_sets_lost_avg_label));
            columns.put(Constants.SETS_WIN_RATE, (TextView) v.findViewById(R.id.tv_sets_per_label));
            columns.put(Constants.BALLS_WON, (TextView) v.findViewById(R.id.tv_balls_won_label));
            columns.put(Constants.BALLS_LOST, (TextView) v.findViewById(R.id.tv_balls_lost_label));
            columns.put(Constants.BALLS_WON_AVG, (TextView) v.findViewById(R.id.tv_balls_won_avg_label));
            columns.put(Constants.BALLS_LOST_AVG, (TextView) v.findViewById(R.id.tv_balls_lost_avg_label));
        }
        return columns;
    }
}
