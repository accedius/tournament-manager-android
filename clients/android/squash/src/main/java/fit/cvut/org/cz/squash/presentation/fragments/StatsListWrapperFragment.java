package fit.cvut.org.cz.squash.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;


/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class StatsListWrapperFragment extends Fragment {

    public static final String ARG_ID = "arg_id";
    public static final String ARG_ACTION = "arg_action";

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
        return inflater.inflate(R.layout.fragment_stats_wrapper, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getChildFragmentManager().findFragmentById(R.id.fragment_container) == null){
            getChildFragmentManager().beginTransaction().add(R.id.fragment_container,
                    AggregatedStatsListFragment.newInstance(getArguments().getLong(ARG_ID), getArguments().getString(ARG_ACTION)))
                    .commit();
        }
    }

}
