package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.hockey.presentation.activities.EditAtOnceActivity;
import fit.cvut.org.cz.hockey.presentation.adapters.MatchStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.dialogs.HomeAwayDialog;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment for player stats in match
 * Created by atgot_000 on 23. 4. 2016.
 */
public class HockeyMatchStatsFragment extends AbstractDataFragment {

    private MatchStatisticsAdapter homeAdapter, awayAdapter;
    private String homeName, awayName;
    private RecyclerView homeRecyclerView, awayRecyclerView;
    private TextView tvHome, tvAway;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    private Fragment thisFragment;
    ArrayList<MatchPlayerStatistic> tmpHomeStats, tmpAwayStats;

    public static final int REQUEST_HOME = 1;
    public static final int REQUEST_AWAY = 2;
    public static final int REQUEST_EDIT = 3;

    private static final String ARG_MATCH_ID = "arg_match_id";
    private static final String SAVE_HOME_LIST = "save_home_list";
    private static final String SAVE_AWAY_LIST = "save_away_list";

    public static HockeyMatchStatsFragment newInstance(long matchId) {
        HockeyMatchStatsFragment fragment = new HockeyMatchStatsFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_MATCH_ID, matchId);
        fragment.setArguments( b );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matchId = getArguments().getLong( ARG_MATCH_ID, -1 );
        thisFragment = this;

        if (savedInstanceState != null) {
            tmpHomeStats = savedInstanceState.getParcelableArrayList(SAVE_HOME_LIST);
            tmpAwayStats = savedInstanceState.getParcelableArrayList(SAVE_AWAY_LIST);
        } else {
            tmpHomeStats = null;
            tmpAwayStats = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tmpHomeStats = homeAdapter.getData();
        tmpAwayStats = awayAdapter.getData();
        outState.putParcelableArrayList(SAVE_HOME_LIST, tmpHomeStats);
        outState.putParcelableArrayList(SAVE_AWAY_LIST, tmpAwayStats);
    }

    @Override
    public void customOnResume(){
        registerReceivers();
        if (!isDataSourceWorking())
            askForData();
        progressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

    }

    @Override
    public void askForData() {
        Intent intent = StatsService.newStartIntent( StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS, getContext());
        intent.putExtra(StatsService.EXTRA_ID, getArguments().getLong(ARG_MATCH_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (tmpHomeStats == null && tmpAwayStats == null) {
            tmpHomeStats = intent.getParcelableArrayListExtra(StatsService.EXTRA_HOME_STATS);
            tmpAwayStats = intent.getParcelableArrayListExtra(StatsService.EXTRA_AWAY_STATS);
        }
        homeName = intent.getStringExtra( StatsService.EXTRA_HOME_NAME );
        awayName = intent.getStringExtra( StatsService.EXTRA_AWAY_NAME );
        homeAdapter.swapData(tmpHomeStats);
        awayAdapter.swapData(tmpAwayStats);
        tvHome.setText(homeName);
        tvAway.setText(awayName);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_stats_wrapper, container, false);

        homeRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_home);
        awayRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_away);
        tvHome = (TextView) fragmentView.findViewById(R.id.tv_home);
        tvAway = (TextView) fragmentView.findViewById(R.id.tv_away);
        scrv = (ScrollView) fragmentView.findViewById(R.id.scroll_v);

        homeAdapter = new MatchStatisticsAdapter( this );
        homeAdapter.setIsHome( true );
        awayAdapter = new MatchStatisticsAdapter( this );
        awayAdapter.setIsHome( false );
        homeRecyclerView.setAdapter(homeAdapter);
        awayRecyclerView.setAdapter(awayAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setNestedScrollingEnabled( false );

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        awayRecyclerView.setLayoutManager(linearLayoutManager2);
        awayRecyclerView.setNestedScrollingEnabled(false);

        fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, (ViewGroup)fragmentView, false);
        ((ViewGroup) fragmentView).addView(fab);

        setOnClickListeners();

        return fragmentView;
    }

    private void setOnClickListeners() {
        scrv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fab.hide();
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    fab.show();
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = homeName+" "+getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.vs)+" "+awayName;
                HomeAwayDialog dialog = HomeAwayDialog.newInstance(homeName, awayName, matchId, title);
                dialog.setTargetFragment(thisFragment, 1);
                dialog.show(getFragmentManager(), "tag211");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AddPlayersActivity.RESULT_OK)
            return;

        if (requestCode == REQUEST_EDIT){
            ArrayList<MatchPlayerStatistic> stats = data.getParcelableArrayListExtra(EditAtOnceActivity.EXTRA_HOME_STATS);
            homeAdapter.swapData( stats );
            tmpHomeStats = stats;
            stats = data.getParcelableArrayListExtra(EditAtOnceActivity.EXTRA_AWAY_STATS);
            awayAdapter.swapData( stats );
            tmpAwayStats = stats;
            return;
        }

        ArrayList<Player> players = data.getParcelableArrayListExtra(SelectableListActivity.EXTRA_DATA);
        ArrayList<MatchPlayerStatistic> playerStatistics;
        if (requestCode == REQUEST_HOME)
            playerStatistics = homeAdapter.getData();
        else
            playerStatistics = awayAdapter.getData();

        for (Player p : players )
            playerStatistics.add( new MatchPlayerStatistic(p.getId(), p.getName(), 0, 0, 0, 0));

        if (requestCode == REQUEST_HOME) {
            homeAdapter.swapData( playerStatistics );
            tmpHomeStats = playerStatistics;
        } else{
            awayAdapter.swapData(playerStatistics);
            tmpAwayStats = playerStatistics;
        }
    }

    public ArrayList<MatchPlayerStatistic> getOmitPlayers() {
        ArrayList<MatchPlayerStatistic> res = new ArrayList<>();
        res.addAll( homeAdapter.getData() );
        res.addAll( awayAdapter.getData() );
        return res;
    }

    /**
     *
     * @return current list of statistics of home team
     */
    public ArrayList<MatchPlayerStatistic> getHomeList()
    {
        return homeAdapter.getData();
    }

    /**
     *
     * @return current list of statistics of away team
     */
    public ArrayList<MatchPlayerStatistic> getAwayList()
    {
        return awayAdapter.getData();
    }

    /**
     * Starts an edit all activity with current data
     */
    public void editAll() {
        tmpHomeStats = getHomeList();
        tmpAwayStats = getAwayList();

        Intent intent = EditAtOnceActivity.newStartIntent( getContext(), tmpHomeStats, tmpAwayStats);
        startActivityForResult( intent, REQUEST_EDIT );
    }

    /**
     * Sets stats of a player. Used by edit stats dialog
     * @param home is it about home team or away team
     * @param position position in data
     * @param statistic statistic to be changed to
     */
    public void setPlayerStats( boolean home, int position, MatchPlayerStatistic statistic ) {
        if (home) {
            ArrayList<MatchPlayerStatistic> dat = homeAdapter.getData();
            dat.remove(position);
            dat.add(position, statistic);
            homeAdapter.swapData( dat );
        } else {
            ArrayList<MatchPlayerStatistic> dat = awayAdapter.getData();
            dat.remove(position);
            dat.add(position, statistic);
            awayAdapter.swapData( dat );
        }
    }

    /**
     * removes player from team
     * @param home is it about home team or away team
     * @param position position in data to be removed
     */
    public void removePlayer( boolean home, int position ){
        if (home) {
            homeAdapter.removePos( position );
        } else{
            awayAdapter.removePos( position );
        }
    }
}
