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
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Header for standings list
 * Created by Vaclav on 10. 4. 2016.
 */
public class StandingsWrapperFragment extends Fragment {
    public static final String ARG_ID = "arg_id";
    private StandingsListFragment slf;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_standings_wrapper, container, false);
        setOrderingListeners(v);
        setDefaultOrder(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long tournamentID = getArguments().getLong(ARG_ID, -1);
        slf = StandingsListFragment.newInstance(tournamentID);
        if (getChildFragmentManager().findFragmentById(R.id.fragment_container2) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container2, slf)
                    .commit();
        }
    }

    /**
     * Bridge for refreshing
     */
    public void refresh(){
        AbstractDataFragment fragment = (AbstractDataFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container2);
        if (fragment != null)
            fragment.customOnResume();
    }

    private void setDefaultOrder(View v) {
        TextView points = (TextView)v.findViewById(R.id.tv_points_label);
        points.setText(points.getText() + " ▼");
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = new HashMap<>();
        columns.put("p", (TextView)v.findViewById(R.id.tv_points_label));
        columns.put("w", (TextView) v.findViewById(R.id.tv_wins_label));
        columns.put("l", (TextView) v.findViewById(R.id.tv_losses_label));
        columns.put("d", (TextView) v.findViewById(R.id.tv_draws_label));

        for (final Map.Entry<String, TextView> e : columns.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slf.orderData(e.getKey(), columns);
                }
            });
        }
    }
}
