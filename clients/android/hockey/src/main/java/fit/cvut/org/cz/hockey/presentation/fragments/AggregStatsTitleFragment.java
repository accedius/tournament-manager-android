package fit.cvut.org.cz.hockey.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment holding the title in player stats
 * Created by atgot_000 on 8. 4. 2016.
 */
public class AggregStatsTitleFragment extends Fragment {

    private long competitionID;
    private long tournamentID;
    private static String ARG_COMP_ID = "competition_id";
    private static String ARG_TOUR_ID = "tournament_id";

    public static AggregStatsTitleFragment newInstance( long id, boolean forComp )
    {
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
        Bundle b = getArguments();
        competitionID = b.getLong(ARG_COMP_ID, -1);
        tournamentID = b.getLong(ARG_TOUR_ID, -1);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            HockeyPlayersStatsFragment clf;

            if( competitionID != -1 ) clf = HockeyPlayersStatsFragment.newInstance(competitionID, true);
            else clf = HockeyPlayersStatsFragment.newInstance(tournamentID, false);

            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, clf)
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
        }
    }
}
