package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
    private long tournamentId;
    ArrayList<MatchPlayerStatistic> tmpHomeStats, tmpAwayStats;
    private static final int REQUEST_HOME = 1;
    private static final int REQUEST_AWAY = 2;
    private static final int REQUEST_EDIT = 3;

    private static final String ARG_MATCH_ID = "arg_match_id";
    //private static final String ARG_TOURNAMENT_ID = "arg_match_id";

    private static final String SAVE_HOME_LIST = "save_home_list";
    private static final String SAVE_AWAY_LIST = "save_away_list";

    public static HockeyMatchStatsFragment newInstance(long matchId)
    {
        HockeyMatchStatsFragment fragment = new HockeyMatchStatsFragment();

        Bundle b = new Bundle();
        b.putLong(ARG_MATCH_ID, matchId);
        //b.putLong(ARG_TOURNAMENT_ID, tournamentId);

        fragment.setArguments( b );
        return fragment;
    }

    //U pridavani playeru muzu omit udelat pres IDcka. neni treba posilat pro playery

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchId = getArguments().getLong( ARG_MATCH_ID, -1 );
        thisFragment = this;

        if( savedInstanceState != null )
        {
            tmpHomeStats = savedInstanceState.getParcelableArrayList(SAVE_HOME_LIST);
            tmpAwayStats = savedInstanceState.getParcelableArrayList(SAVE_AWAY_LIST);
        }
        else{
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
    protected void customOnResume(){
        registerReceivers();
        if (!isDataSourceWorking())
            askForData();
        progressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

    }

    @Override
    protected void askForData() {
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
        if( tmpHomeStats == null && tmpAwayStats == null ) {
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
        awayAdapter = new MatchStatisticsAdapter( this );
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

    private void setOnClickListeners()
    {
        scrv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fab.hide();
                if( event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL )
                {
                    fab.show();
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomeAwayDialog dialog = new HomeAwayDialog() {
                    @Override
                    protected void homeClicked() {
                        ArrayList<MatchPlayerStatistic> omitStats = getOmitPlayers();
                        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, matchId);
                        intent.putParcelableArrayListExtra(AddPlayersActivity.EXTRA_OMIT_DATA, omitStats);
                        thisFragment.startActivityForResult(intent, REQUEST_HOME);
                    }

                    @Override
                    protected void awayClicked() {
                        ArrayList<MatchPlayerStatistic> omitStats = getOmitPlayers();
                        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, matchId);
                        intent.putParcelableArrayListExtra(AddPlayersActivity.EXTRA_OMIT_DATA, omitStats);
                        thisFragment.startActivityForResult(intent, REQUEST_AWAY);
                    }

                    @Override
                    protected String getHomeName() {
                        return homeName;
                    }

                    @Override
                    protected String getAwayName() {
                        return awayName;
                    }
                };
                dialog.show(getFragmentManager(), "tag211");
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != AddPlayersActivity.RESULT_OK) return;

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
        if( requestCode == REQUEST_HOME ) playerStatistics = homeAdapter.getData();
        else playerStatistics = awayAdapter.getData();
        for (Player p : players )
            playerStatistics.add( new MatchPlayerStatistic(p.getId(), p.getName(), 0, 0, 0, 0));
        if( requestCode == REQUEST_HOME ) {
            homeAdapter.swapData( playerStatistics );
            tmpHomeStats = playerStatistics;
        }
        else{
            awayAdapter.swapData(playerStatistics);
            tmpAwayStats = playerStatistics;
        }
    }

    private ArrayList<MatchPlayerStatistic> getOmitPlayers()
    {
        ArrayList<MatchPlayerStatistic> res = new ArrayList<>();
        res.addAll( homeAdapter.getData() );
        res.addAll( awayAdapter.getData() );
        return res;
    }

    public ArrayList<MatchPlayerStatistic> getHomeList()
    {
        return homeAdapter.getData();
    }

    public ArrayList<MatchPlayerStatistic> getAwayList()
    {
        return awayAdapter.getData();
    }

    public void editAll() {
        MatchStatisticsAdapter adapter = homeAdapter;
        adapter = awayAdapter;

        tmpHomeStats = getHomeList();
        tmpAwayStats = getAwayList();

        Intent intent = EditAtOnceActivity.newStartIntent( getContext(), tmpHomeStats, tmpAwayStats);

        startActivityForResult( intent, REQUEST_EDIT );
    }

}
