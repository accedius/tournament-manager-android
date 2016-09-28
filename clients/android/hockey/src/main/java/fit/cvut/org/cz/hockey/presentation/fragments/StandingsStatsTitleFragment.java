package fit.cvut.org.cz.hockey.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment holding the header for standings
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsStatsTitleFragment extends Fragment {
    private static String ARG_TOUR_ID = "tournament_id";
    private StandingsFragment sf;

    public static StandingsStatsTitleFragment newInstance(long id) {
        StandingsStatsTitleFragment fragment = new StandingsStatsTitleFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TOUR_ID, id);
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
        Long tournamentID = getArguments().getLong(ARG_TOUR_ID, -1);

        sf = StandingsFragment.newInstance(tournamentID);
        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, sf)
                    .commit();
        }
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
        points.setText(points.getText() + " ▼");
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = new HashMap<>();
        columns.put("p", (TextView)v.findViewById(R.id.standings_points));
        if (v.findViewById(R.id.standings_total_wins) != null) {
            columns.put("tw", (TextView) v.findViewById(R.id.standings_total_wins));
            columns.put("d", (TextView) v.findViewById(R.id.standings_draws));
            columns.put("tl", (TextView) v.findViewById(R.id.standings_total_losses));
        } else {
            columns.put("w", (TextView) v.findViewById(R.id.standings_wins));
            columns.put("d", (TextView) v.findViewById(R.id.standings_draws));
            columns.put("l", (TextView) v.findViewById(R.id.standings_losses));
            columns.put("wot", (TextView) v.findViewById(R.id.standings_wins_ot));
            columns.put("lot", (TextView) v.findViewById(R.id.standings_losses_ot));
            columns.put("wso", (TextView) v.findViewById(R.id.standings_wins_so));
            columns.put("lso", (TextView) v.findViewById(R.id.standings_losses_so));
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
