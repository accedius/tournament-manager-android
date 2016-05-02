package fit.cvut.org.cz.hockey.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.adapters.EditableStatsAdapter;

/**
 * Created by atgot_000 on 1. 5. 2016.
 */
public class MatchEditAtOnceFragment extends Fragment {

    private EditableStatsAdapter homeAdp, awayAdp;
    private RecyclerView homeRecyclerView, awayRecyclerView;

    private ArrayList<MatchPlayerStatistic> tmpHomeStat, tmpAwayStat;

    private static final String ARG_HOME = "arg_home";
    private static final String ARG_AWAY = "arg_away";

    public static MatchEditAtOnceFragment newInstance(ArrayList<MatchPlayerStatistic> homeStats, ArrayList<MatchPlayerStatistic> awayStats)
    {
        MatchEditAtOnceFragment fragment = new MatchEditAtOnceFragment();

        ArrayList<MatchPlayerStatistic> newHomeStat = new ArrayList<>(homeStats);
        ArrayList<MatchPlayerStatistic> newAwayStat = new ArrayList<>(awayStats);

        Bundle b = new Bundle();
        b.putParcelableArrayList(ARG_HOME, newHomeStat);
        b.putParcelableArrayList(ARG_AWAY, newAwayStat);

        fragment.setArguments( b );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_edit_at_once, container, false);

        homeRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_home);
        awayRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_away);

        homeAdp = new EditableStatsAdapter();
        awayAdp = new EditableStatsAdapter();

        homeRecyclerView.setAdapter(homeAdp);
        awayRecyclerView.setAdapter(awayAdp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        awayRecyclerView.setLayoutManager(linearLayoutManager2);
        awayRecyclerView.setNestedScrollingEnabled(false);

        homeAdp.swapData(tmpHomeStat);
        awayAdp.swapData(tmpAwayStat);

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<MatchPlayerStatistic> homeData, awayData;

        if( savedInstanceState != null ){
            homeData = savedInstanceState.getParcelableArrayList(ARG_HOME);
            awayData = savedInstanceState.getParcelableArrayList(ARG_AWAY);
        } else {
            homeData = getArguments().getParcelableArrayList( ARG_HOME );
            awayData = getArguments().getParcelableArrayList( ARG_AWAY );
        }
        tmpHomeStat = homeData;
        tmpAwayStat = awayData;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(ARG_HOME, homeAdp.getData());
        outState.putParcelableArrayList(ARG_AWAY, awayAdp.getData());
    }

    public ArrayList<MatchPlayerStatistic> getHomeData(){
        return new ArrayList<>(homeAdp.getData());
    }

    public ArrayList<MatchPlayerStatistic> getAwayData(){
        return new ArrayList<>(awayAdp.getData());
    }
}
