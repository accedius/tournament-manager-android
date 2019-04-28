package fit.cvut.org.cz.bowling.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public class AggregStatsTitleFragment extends Fragment {
    private BowlingPlayersStatsFragment statsFragment;

    public static AggregStatsTitleFragment newInstance(long id, boolean forComp) {
        AggregStatsTitleFragment fragment = new AggregStatsTitleFragment();
        Bundle args = new Bundle();

        if (forComp)
            args.putLong(ExtraConstants.EXTRA_COMP_ID, id);
        else
            args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_stats_title, container, false);
        setOrderingListeners(v);
        setDefaultOrder(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Long competitionID = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
        //Long tournamentID = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);

        if (competitionID != -1) {
            statsFragment = BowlingPlayersStatsFragment.newInstance(competitionID, true);
        } else {
            //statsFragment = BowlingPlayersStatsFragment.newInstance(tournamentID, false);
        }

        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, statsFragment)
                    .commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_help, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
    public void refresh(){
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.stats_list);
        if (fr != null && fr instanceof AbstractDataFragment) {
            ((AbstractDataFragment) fr).customOnResume();
            setDefaultOrder(getView());
        }
    }
    */

    private void setDefaultOrder(View v) {
        deleteOtherOrders(v);
        TextView points = (TextView)v.findViewById(R.id.stats_points);
        points.setText(points.getText() + " "+Constants.DESC_SIGN);
    }

    private void deleteOtherOrders(View v) {
        HashMap<String, TextView> columns = getColumns(v);
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
        columns.put(Constants.MATCHES,(TextView)v.findViewById(R.id.stats_games_played));
        columns.put(Constants.STRIKES, (TextView)v.findViewById(R.id.stats_strikes));
        columns.put(Constants.SPARES, (TextView)v.findViewById(R.id.stats_spares));
        columns.put(Constants.POINTS, (TextView)v.findViewById(R.id.stats_points));
        if (v.findViewById(R.id.stats_wins) != null) {
            columns.put(Constants.WINS, (TextView) v.findViewById(R.id.stats_wins));
            columns.put(Constants.DRAWS, (TextView) v.findViewById(R.id.stats_draws));
            columns.put(Constants.LOSSES, (TextView) v.findViewById(R.id.stats_losses));
            columns.put(Constants.TEAM_POINTS, (TextView) v.findViewById(R.id.stats_team_points));
            columns.put(Constants.STRIKES_AVG, (TextView) v.findViewById(R.id.stats_strikes_avg));
            columns.put(Constants.POINTS_AVG, (TextView) v.findViewById(R.id.stats_points_avg));
            columns.put(Constants.TEAM_POINTS_AVG, (TextView) v.findViewById(R.id.stats_team_points_avg));
        }
        return columns;
    }
}
