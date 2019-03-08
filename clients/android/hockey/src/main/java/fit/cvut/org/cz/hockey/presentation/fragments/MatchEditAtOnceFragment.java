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
import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.adapters.EditableStatsAdapter;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;

/**
 * Fragment for editing all stats in match at once
 * Created by atgot_000 on 1. 5. 2016.
 */
public class MatchEditAtOnceFragment extends Fragment {
    private EditableStatsAdapter homeAdp, awayAdp;
    private RecyclerView homeRecyclerView, awayRecyclerView;
    private List<PlayerStat> tmpHomeStat, tmpAwayStat;

    public static MatchEditAtOnceFragment newInstance(ArrayList<PlayerStat> homeStats, ArrayList<PlayerStat> awayStats) {
        MatchEditAtOnceFragment fragment = new MatchEditAtOnceFragment();

        Bundle b = new Bundle();
        b.putParcelableArrayList(ExtraConstants.EXTRA_HOME_STATS, homeStats);
        b.putParcelableArrayList(ExtraConstants.EXTRA_AWAY_STATS, awayStats);

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
            homeData = savedInstanceState.getParcelableArrayList(ExtraConstants.EXTRA_HOME_STATS);
            awayData = savedInstanceState.getParcelableArrayList(ExtraConstants.EXTRA_AWAY_STATS);
        } else {
            homeData = getArguments().getParcelableArrayList(ExtraConstants.EXTRA_HOME_STATS);
            awayData = getArguments().getParcelableArrayList(ExtraConstants.EXTRA_AWAY_STATS);
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
        outState.putParcelableArrayList(ExtraConstants.EXTRA_HOME_STATS, homeAdpArrayList);
        outState.putParcelableArrayList(ExtraConstants.EXTRA_AWAY_STATS, awayAdpArrayList);
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
