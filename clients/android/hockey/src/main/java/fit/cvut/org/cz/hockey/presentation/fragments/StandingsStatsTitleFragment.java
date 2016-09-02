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
 * Fragment holding the header for standings
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsStatsTitleFragment extends Fragment {

    private long tournamentID;
    private static String ARG_TOUR_ID = "tournament_id";

    public static StandingsStatsTitleFragment newInstance( long id ) {
        StandingsStatsTitleFragment fragment = new StandingsStatsTitleFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TOUR_ID, id);
        fragment.setArguments( args );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_standings_title, container, false);
        Bundle b = getArguments();
        tournamentID = b.getLong(ARG_TOUR_ID, -1);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            StandingsFragment clf;
            clf = StandingsFragment.newInstance(tournamentID);
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
