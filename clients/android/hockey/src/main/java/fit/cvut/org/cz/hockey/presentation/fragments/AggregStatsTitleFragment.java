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
 * Fragment holding the title in player stats
 * Created by atgot_000 on 8. 4. 2016.
 */
public class AggregStatsTitleFragment extends Fragment {

    private static String ARG_COMP_ID = "competition_id";
    private static String ARG_TOUR_ID = "tournament_id";

    private HockeyPlayersStatsFragment statsFragment;

    public static AggregStatsTitleFragment newInstance(long id, boolean forComp) {
        AggregStatsTitleFragment fragment = new AggregStatsTitleFragment();
        Bundle args = new Bundle();

        if( forComp ) args.putLong(ARG_COMP_ID, id);
        else args.putLong(ARG_TOUR_ID, id);

        fragment.setArguments( args );
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
        Long competitionID = getArguments().getLong(ARG_COMP_ID, -1);
        Long tournamentID = getArguments().getLong(ARG_TOUR_ID, -1);

        if( competitionID != -1 ) {
            statsFragment = HockeyPlayersStatsFragment.newInstance(competitionID, true);
        } else {
            statsFragment = HockeyPlayersStatsFragment.newInstance(tournamentID, false);
        }

        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, statsFragment)
                    .commit();
        }
    }

    /**
     * Bridge for refreshing
     */
    public void refresh(){
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.stats_list);
        if (fr != null && fr instanceof AbstractDataFragment){
            ((AbstractDataFragment) fr).customOnResume();
            // TODO ((HockeyPlayersStatsFragment) fr). ... setDefaultOrder ! (zobrazit ▼ u bodu)
        }
    }

    private void setDefaultOrder(View v) {
        TextView points = (TextView)v.findViewById(R.id.stats_points);
        points.setText(points.getText() + " ▼");
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = new HashMap<>();
        columns.put("gp",(TextView)v.findViewById(R.id.stats_games_played));
        columns.put("g", (TextView)v.findViewById(R.id.stats_goals));
        columns.put("a", (TextView)v.findViewById(R.id.stats_assists));
        columns.put("p", (TextView)v.findViewById(R.id.stats_points));
        columns.put("+-", (TextView)v.findViewById(R.id.stats_plus_minus));
        columns.put("s", (TextView)v.findViewById(R.id.stats_saves));
        if (v.findViewById(R.id.stats_wins) != null) {
            columns.put("w", (TextView) v.findViewById(R.id.stats_wins));
            columns.put("d", (TextView) v.findViewById(R.id.stats_draws));
            columns.put("l", (TextView) v.findViewById(R.id.stats_losses));
            columns.put("tp", (TextView) v.findViewById(R.id.stats_team_points));
            columns.put("gavg", (TextView) v.findViewById(R.id.stats_goals_avg));
            columns.put("pavg", (TextView) v.findViewById(R.id.stats_points_avg));
            columns.put("+-avg", (TextView) v.findViewById(R.id.stats_plus_minus_avg));
            columns.put("tpavg", (TextView) v.findViewById(R.id.stats_team_points_avg));
        }
        for(final Map.Entry<String, TextView> e : columns.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statsFragment.orderData(e.getKey(), columns);
                }
            });
        }
    }
}
