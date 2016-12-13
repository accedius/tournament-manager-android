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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.hockey.presentation.activities.EditAtOnceActivity;
import fit.cvut.org.cz.hockey.presentation.adapters.MatchStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.dialogs.HomeAwayDialog;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment for player stats in match
 * Created by atgot_000 on 23. 4. 2016.
 */
public class HockeyMatchStatsFragment extends AbstractDataFragment {
    private MatchStatisticsAdapter homeAdapter, awayAdapter;
    private String homeName, awayName;
    private Participant home = null, away = null;
    private RecyclerView homeRecyclerView, awayRecyclerView;
    private TextView tvHome, tvAway;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    List<PlayerStat> tmpHomeStats, tmpAwayStats;

    public static final int REQUEST_HOME = 1;
    public static final int REQUEST_AWAY = 2;
    public static final int REQUEST_EDIT = 3;

    private static final String ARG_MATCH_ID = "arg_match_id";
    private static final String SAVE_HOME_LIST = "save_home_list";
    private static final String SAVE_AWAY_LIST = "save_away_list";
    private static final String SAVE_HOME = "save_home";
    private static final String SAVE_AWAY = "save_away";
    private Fragment thisFragment;

    public static HockeyMatchStatsFragment newInstance(long matchId) {
        HockeyMatchStatsFragment fragment = new HockeyMatchStatsFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_MATCH_ID, matchId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        matchId = getArguments().getLong(ARG_MATCH_ID, -1);

        if (savedInstanceState != null) {
            tmpHomeStats = savedInstanceState.getParcelableArrayList(SAVE_HOME_LIST);
            tmpAwayStats = savedInstanceState.getParcelableArrayList(SAVE_AWAY_LIST);
            home = savedInstanceState.getParcelable(SAVE_HOME);
            away = savedInstanceState.getParcelable(SAVE_AWAY);
        } else {
            tmpHomeStats = null;
            tmpAwayStats = null;
        }
        thisFragment = this;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_match_stats, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tmpHomeStats = homeAdapter.getData();
        tmpAwayStats = awayAdapter.getData();
        outState.putParcelable(SAVE_HOME, home);
        outState.putParcelable(SAVE_AWAY, away);
        outState.putParcelableArrayList(SAVE_HOME_LIST, new ArrayList<>(tmpHomeStats));
        outState.putParcelableArrayList(SAVE_AWAY_LIST, new ArrayList<>(tmpAwayStats));
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
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS, getContext());
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
        homeName = intent.getStringExtra(StatsService.EXTRA_HOME_NAME);
        awayName = intent.getStringExtra(StatsService.EXTRA_AWAY_NAME);
        home = intent.getParcelableExtra(StatsService.EXTRA_HOME_PARTICIPANT);
        away = intent.getParcelableExtra(StatsService.EXTRA_AWAY_PARTICIPANT);
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

        homeAdapter = new MatchStatisticsAdapter(this);
        homeAdapter.setIsHome(true);
        awayAdapter = new MatchStatisticsAdapter(this);
        awayAdapter.setIsHome(false);
        homeRecyclerView.setAdapter(homeAdapter);
        awayRecyclerView.setAdapter(awayAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setNestedScrollingEnabled(false);

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
                HomeAwayDialog dialog = HomeAwayDialog.newInstance(homeName, awayName, matchId);
                dialog.setTargetFragment(thisFragment, 0);
                dialog.show(getFragmentManager(), "tag211");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AddPlayersActivity.RESULT_OK)
            return;

        if (requestCode == REQUEST_EDIT) {
            ArrayList<PlayerStat> homeStats = data.getParcelableArrayListExtra(EditAtOnceActivity.EXTRA_HOME_PLAYERS_STATS);
            homeAdapter.swapData(homeStats);
            tmpHomeStats = homeStats;
            ArrayList<PlayerStat> awayStats = data.getParcelableArrayListExtra(EditAtOnceActivity.EXTRA_AWAY_PLAYERS_STATS);
            awayAdapter.swapData(awayStats);
            tmpAwayStats = awayStats;
            return;
        }

        Participant participant;
        ArrayList<Player> players = data.getParcelableArrayListExtra(SelectableListActivity.EXTRA_DATA);
        List<PlayerStat> playerStatistics;
        if (requestCode == REQUEST_HOME) {
            playerStatistics = homeAdapter.getData();
            participant = home;
        }
        else {
            playerStatistics = awayAdapter.getData();
            participant = away;
        }

        for (Player player : players) {
            PlayerStat playerStat = new PlayerStat(participant.getId(), player.getId());
            playerStat.setName(player.getName());
            playerStatistics.add(playerStat);
        }

        if (requestCode == REQUEST_HOME) {
            homeAdapter.swapData(playerStatistics);
            tmpHomeStats = playerStatistics;
        } else {
            awayAdapter.swapData(playerStatistics);
            tmpAwayStats = playerStatistics;
        }
    }

    public ArrayList<PlayerStat> getOmitPlayers() {
        ArrayList<PlayerStat> res = new ArrayList<>();
        res.addAll(homeAdapter.getData());
        res.addAll(awayAdapter.getData());
        return res;
    }

    /**
     *
     * @return current list of statistics of home team
     */
    public List<PlayerStat> getHomeList() {
        return homeAdapter.getData();
    }

    /**
     *
     * @return current list of statistics of away team
     */
    public List<PlayerStat> getAwayList() {
        return awayAdapter.getData();
    }

    /**
     * Starts an edit all activity with current data
     */
    public void editAll() {
        tmpHomeStats = getHomeList();
        tmpAwayStats = getAwayList();

        Intent intent = EditAtOnceActivity.newStartIntent(getContext(), tmpHomeStats, tmpAwayStats);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    /**
     * Sets stats of a player. Used by edit stats dialog
     * @param home is it about home team or away team
     * @param position position in data
     * @param statistic statistic to be changed to
     */
    public void setPlayerStats(boolean home, int position, PlayerStat statistic) {
        if (home) {
            List<PlayerStat> dat = homeAdapter.getData();
            dat.remove(position);
            dat.add(position, statistic);
            homeAdapter.swapData(dat);
        } else {
            List<PlayerStat> dat = awayAdapter.getData();
            dat.remove(position);
            dat.add(position, statistic);
            awayAdapter.swapData(dat);
        }
    }

    /**
     * removes player from team
     * @param home is it about home team or away team
     * @param position position in data to be removed
     */
    public void removePlayer(boolean home, int position){
        if (home) {
            homeAdapter.removePos(position);
        } else{
            awayAdapter.removePos(position);
        }
    }
}
