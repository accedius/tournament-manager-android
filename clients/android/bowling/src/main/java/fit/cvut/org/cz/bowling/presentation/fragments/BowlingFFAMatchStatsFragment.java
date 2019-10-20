package fit.cvut.org.cz.bowling.presentation.fragments;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.activities.EditAtOnceActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.MatchStatisticsAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.HomeAwayDialog;
import fit.cvut.org.cz.bowling.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment is used in
 */
public class BowlingFFAMatchStatsFragment extends AbstractDataFragment {
    private static final String SAVE_LIST = "save_list";
    private static final String SAVE_PART = "save_part";

    public static final int REQUEST_PART = 1;
    public static final int REQUEST_EDIT = 3;

    private MatchStatisticsAdapter partAdapter;

    private Participant part = null;
    private RecyclerView recyclerView;
    private TextView tvPart;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    List<PlayerStat> tmpPartStats;
    private Fragment thisFragment;

    public static BowlingFFAMatchStatsFragment newInstance(long matchId) {
        BowlingFFAMatchStatsFragment fragment = new BowlingFFAMatchStatsFragment();
        Bundle b = new Bundle();
        b.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);

        if (savedInstanceState != null) {
            tmpPartStats = savedInstanceState.getParcelableArrayList(SAVE_LIST);
            part = savedInstanceState.getParcelable(SAVE_PART);
        } else {
            tmpPartStats = null;
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
        tmpPartStats = partAdapter.getData();
        outState.putParcelable(SAVE_PART, part);
        outState.putParcelableArrayList(SAVE_LIST, new ArrayList<>(tmpPartStats));
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
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (tmpPartStats == null) {
            tmpPartStats = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS);
        }
        part = intent.getParcelableExtra(ExtraConstants.EXTRA_HOME_PARTICIPANT);
        partAdapter.swapData(tmpPartStats);
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
        View fragmentView = inflater.inflate(R.layout.fragment_ffa_match_stats_wrapper, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_home);

        tvPart = (TextView) fragmentView.findViewById(R.id.tv_home);
        scrv = (ScrollView) fragmentView.findViewById(R.id.scroll_v);

        partAdapter = new MatchStatisticsAdapter(this);
        partAdapter.setIsHome(true);
        recyclerView.setAdapter(partAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

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
                ArrayList<PlayerStat> omitStats = ((BowlingMatchStatsFragment)getTargetFragment()).getOmitPlayers();
                Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitStats);
                getTargetFragment().startActivityForResult(intent, BowlingFFAMatchStatsFragment.REQUEST_PART);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AddPlayersActivity.RESULT_OK)
            return;

        if (requestCode == REQUEST_EDIT) {
            ArrayList<PlayerStat> homeStats = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS);
            partAdapter.swapData(homeStats);
            tmpPartStats = homeStats;
            return;
        }

        Participant participant;
        ArrayList<Player> players = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA);
        List<PlayerStat> playerStatistics;
            playerStatistics = partAdapter.getData();
            participant = part;

        for (Player player : players) {
            //Participant part = new Participant(matchId, player.getId(), ParticipantType.home.toString());
            PlayerStat playerStat = new PlayerStat(participant.getId(), player.getId());
            playerStat.setName(player.getName());
            playerStatistics.add(playerStat);
        }

            partAdapter.swapData(playerStatistics);
            tmpPartStats = playerStatistics;
    }

    public ArrayList<PlayerStat> getOmitPlayers() {
        ArrayList<PlayerStat> res = new ArrayList<>();
        res.addAll(partAdapter.getData());
        return res;
    }

    /**
     *
     * @return current list of statistics of part team
     */
    public List<PlayerStat> getHomeList() {
        return partAdapter.getData();
    }

    /**
     * Starts an edit all activity with current data
     */
    public void editAll() {
        //tmpPartStats = getHomeList();

        //Intent intent = EditAtOnceActivity.newStartIntent(getContext(), tmpPartStats, tmpAwayStats);
        //startActivityForResult(intent, REQUEST_EDIT);
    }

    /**
     * Sets stats of a player. Used by edit stats dialog
     * @param home is it about part team or away team
     * @param position position in data
     * @param statistic statistic to be changed to
     */
    public void setPlayerStats(boolean home, int position, PlayerStat statistic) {
        if (home) {
            List<PlayerStat> dat = partAdapter.getData();
            dat.remove(position);
            dat.add(position, statistic);
            partAdapter.swapData(dat);
        }
    }

    /**
     * removes player from team
     * @param home is it about part team or away team
     * @param position position in data to be removed
     */
    public void removePlayer(boolean home, int position){
        if (home) {
            partAdapter.removePos(position);
        }
    }
}
