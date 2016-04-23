package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.adapters.MatchStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by atgot_000 on 23. 4. 2016.
 */
public class HockeyMatchStatsFragment extends AbstractDataFragment {

    private MatchStatisticsAdapter homeAdapter, awayAdapter;
    private String homeName, awayName;
    private RecyclerView homeRecyclerView, awayRecyclerView;
    private long matchId;

    private static final String ARG_MATCH_ID = "arg_match_id";

    private static final String SAVE_HOME_LIST = "save_home_list";
    private static final String SAVE_AWAY_LIST = "save_away_list";

    public HockeyMatchStatsFragment newInstance(long matchId)
    {
        HockeyMatchStatsFragment fragment = new HockeyMatchStatsFragment();

        Bundle b = new Bundle();
        b.putLong(ARG_MATCH_ID, matchId);

        fragment.setArguments( b );
        return fragment;
    }

    //U pridavani playeru muzu omit udelat pres IDcka. neni treba posilat pro playery

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TODO naplnit ze savedState a dodelat saveState
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void askForData() {
        Intent intent = StatsService.newStartIntent( StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS, getContext());
        intent.putExtra( StatsService.EXTRA_ID, matchId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void bindDataOnView(Intent intent) {

    }

    @Override
    protected void registerReceivers() {

    }

    @Override
    protected void unregisterReceivers() {

    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_stats_wrapper, container, false);
        return fragmentView;
    }

    public ArrayList<MatchPlayerStatistic> getHomeList()
    {
        //TODO
        return null;
    }

    public ArrayList<MatchPlayerStatistic> getAwayList()
    {
        //TODO
        return null;
    }
}
