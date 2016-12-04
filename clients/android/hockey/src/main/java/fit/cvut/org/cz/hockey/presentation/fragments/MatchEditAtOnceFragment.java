package fit.cvut.org.cz.hockey.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.adapters.EditableStatsAdapter;

/**
 * Fragment for editing all stats in match at once
 * Created by atgot_000 on 1. 5. 2016.
 */
public class MatchEditAtOnceFragment extends Fragment {
    private EditableStatsAdapter homeAdp, awayAdp;
    private RecyclerView homeRecyclerView, awayRecyclerView;
    private List<PlayerStat> tmpHomeStat, tmpAwayStat;

    private static final String ARG_HOME = "arg_home";
    private static final String ARG_AWAY = "arg_away";

    public static MatchEditAtOnceFragment newInstance(ArrayList<PlayerStat> homeStats, ArrayList<PlayerStat> awayStats) {
        MatchEditAtOnceFragment fragment = new MatchEditAtOnceFragment();

        Bundle b = new Bundle();
        b.putParcelableArrayList(ARG_HOME, homeStats);
        b.putParcelableArrayList(ARG_AWAY, awayStats);

        fragment.setArguments(b);
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
        ArrayList<PlayerStat> homeData, awayData;

        if (savedInstanceState != null) {
            homeData = savedInstanceState.getParcelableArrayList(ARG_HOME);
            awayData = savedInstanceState.getParcelableArrayList(ARG_AWAY);
        } else {
            homeData = getArguments().getParcelableArrayList(ARG_HOME);
            awayData = getArguments().getParcelableArrayList(ARG_AWAY);
        }
        tmpHomeStat = homeData;
        tmpAwayStat = awayData;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<PlayerStat> homeAdpArrayList = new ArrayList<>();
        homeAdpArrayList.addAll(homeAdp.getData());
        ArrayList<PlayerStat> awayAdpArrayList = new ArrayList<>();
        awayAdpArrayList.addAll(awayAdp.getData());
        outState.putParcelableArrayList(ARG_HOME, homeAdpArrayList);
        outState.putParcelableArrayList(ARG_AWAY, awayAdpArrayList);
    }

    /**
     *
     * @return current list of statistics for home team
     */
    public ArrayList<PlayerStat> getHomeData(){
        return new ArrayList<>(homeAdp.getData());
    }

    /**
     *
     * @return current list of statistics for home team
     */
    public ArrayList<PlayerStat> getAwayData(){
        return new ArrayList<>(awayAdp.getData());
    }
}
