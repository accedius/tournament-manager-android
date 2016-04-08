package fit.cvut.org.cz.hockey.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class AgregStatsTitleFragment extends Fragment {

    private long competitionID;
    private static String ARG_ID = "competition_id";

    public static AgregStatsTitleFragment newInstance( long id )
    {
        AgregStatsTitleFragment fragment = new AgregStatsTitleFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments( args );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_stats_title, container, false);
        Bundle b = getArguments();
        competitionID = b.getLong(ARG_ID);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            HockeyCompetitionPlayersFragment clf = HockeyCompetitionPlayersFragment.newInstance( competitionID );
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, clf)
                    .commit();
        }
    }
}
