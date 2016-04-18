package fit.cvut.org.cz.squash.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;


/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class StandingsWrapperFragment extends Fragment {

    public static final String ARG_ID = "arg_id";

    public static StandingsWrapperFragment newInstance(long id){
        StandingsWrapperFragment fragment = new StandingsWrapperFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_standings_wrapper, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getChildFragmentManager().findFragmentById(R.id.fragment_container2) == null){
            getChildFragmentManager().beginTransaction().add(R.id.fragment_container2,
                    StandingsListFragment.newInstance(getArguments().getLong(ARG_ID)))
                    .commit();
        }
    }

}
