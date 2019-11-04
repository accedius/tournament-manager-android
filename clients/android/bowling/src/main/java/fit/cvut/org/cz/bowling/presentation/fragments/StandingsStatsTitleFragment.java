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

/**
 * Fragment is used in ShowTournamentActivity to show standings stats panel, particularly standings' grid and it's contents
 */
public class StandingsStatsTitleFragment extends Fragment {
    private StandingsFragment sf;

    public static StandingsStatsTitleFragment newInstance(long id) {
        StandingsStatsTitleFragment fragment = new StandingsStatsTitleFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_standings_title, container, false);
        setOrderingListeners(v);
        setDefaultOrder(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Long tournamentID = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);

        sf = StandingsFragment.newInstance(tournamentID);
        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, sf)
                    .commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_help, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Bridge for refreshing
     */
    public void refresh(){
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.stats_list);
        if (fr != null && fr instanceof AbstractDataFragment) {
            ((AbstractDataFragment) fr).customOnResume();
        }
    }

    private void setDefaultOrder(View v) {
        TextView points = (TextView)v.findViewById(R.id.standings_points);
        points.setText(points.getText() + " "+Constants.DESC_SIGN);
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = new HashMap<>();
        columns.put(Constants.MATCHES, (TextView)v.findViewById(R.id.standings_games_played));
        columns.put(Constants.POINTS, (TextView)v.findViewById(R.id.standings_points));
        columns.put(Constants.SCORE, (TextView)v.findViewById(R.id.standings_score));
        if (v.findViewById(R.id.standings_total_wins) != null) {
            columns.put(Constants.WINS_TOTAL, (TextView) v.findViewById(R.id.standings_total_wins));
            columns.put(Constants.DRAWS, (TextView) v.findViewById(R.id.standings_draws));
            columns.put(Constants.LOSSES_TOTAL, (TextView) v.findViewById(R.id.standings_total_losses));
        } else {
            columns.put(Constants.WINS, (TextView) v.findViewById(R.id.standings_wins));
            columns.put(Constants.DRAWS, (TextView) v.findViewById(R.id.standings_draws));
            columns.put(Constants.LOSSES, (TextView) v.findViewById(R.id.standings_losses));
            columns.put(Constants.WINS_OT, (TextView) v.findViewById(R.id.standings_wins_ot));
            columns.put(Constants.LOSSES_OT, (TextView) v.findViewById(R.id.standings_losses_ot));
            columns.put(Constants.WINS_SO, (TextView) v.findViewById(R.id.standings_wins_so));
            columns.put(Constants.LOSSES_SO, (TextView) v.findViewById(R.id.standings_losses_so));
        }
        for (final Map.Entry<String, TextView> e : columns.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sf.orderData(e.getKey(), columns);
                }
            });
        }
    }
}
