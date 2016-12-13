package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import java.util.List;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.adapters.SimplePlayerAdapter;
import fit.cvut.org.cz.squash.presentation.dialogs.AdapterDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.SelectTeamDialog;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Displays roster in match detail
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchPlayersFragment extends AbstractDataFragment {
    private static final String ARG_ID = "arg_id";
    private static final String SAVE_HOME = "save_home";
    private static final String SAVE_AWAY = "save_away";
    private static final String SAVE_HOME_PLAYERS = "save_home_players";
    private static final String SAVE_AWAY_PLAYERS = "save_away_players";
    private static final String SAVE_DATA = "save_data";
    public static final int REQUEST_HOME = 0;
    public static final int REQUEST_AWAY = 1;

    public SimplePlayerAdapter homeAdapter = null, awayAdapter = null;
    private TextView home = null, away = null;
    private boolean askForData = true;

    private String homeName = null, awayName = null;
    private Participant homeParticipant = null, awayParticipant = null;
    private ArrayList<PlayerStat> homePlayers = null, awayPlayers = null;

    public static MatchPlayersFragment newInstance(long id){
        MatchPlayersFragment f = new MatchPlayersFragment();
        Bundle b = new Bundle();
        b.putLong(ARG_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_PLAYERS_IN_MATCH, getContext());
        intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(PlayerService.ACTION_GET_PLAYERS_IN_MATCH);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        askForData = false;
        unregisterReceivers();
        homeParticipant = intent.getParcelableExtra(PlayerService.EXTRA_HOME_PARTICIPANT);
        awayParticipant = intent.getParcelableExtra(PlayerService.EXTRA_AWAY_PARTICIPANT);
        homeName = intent.getStringExtra(PlayerService.EXTRA_HOME_NAME);
        awayName = intent.getStringExtra(PlayerService.EXTRA_AWAY_NAME);
        home.setText(homeName);
        away.setText(awayName);

        homePlayers = intent.getParcelableArrayListExtra(PlayerService.EXTRA_HOME_PLAYERS);
        awayPlayers = intent.getParcelableArrayListExtra(PlayerService.EXTRA_AWAY_PLAYERS);
        homeAdapter.swapData(homePlayers);
        awayAdapter.swapData(awayPlayers);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(PlayerService.ACTION_GET_PLAYERS_IN_MATCH));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
       View v = inflater.inflate(R.layout.fragment_match_players_improved, container, false);

        homeAdapter = new SimplePlayerAdapter(){
            @Override
            protected void setOnClickListeners(View itemView, final int position, final String name) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AdapterDialog d = AdapterDialog.newInstance(position, 0, name);
                        d.setTargetFragment(MatchPlayersFragment.this, 0);
                        d.show(getFragmentManager(), "DELETE_HOME");
                        return false;
                    }
                });
            }
        };
        awayAdapter = new SimplePlayerAdapter(){
            @Override
            protected void setOnClickListeners(View itemView, final int position, final String name) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AdapterDialog d = AdapterDialog.newInstance(position, 1, name);
                        d.setTargetFragment(MatchPlayersFragment.this, 0);
                        d.show(getFragmentManager(), "DELETE_AWAY");
                        return false;
                    }
                });
            }
        };
        home = (TextView) v.findViewById(R.id.tv_home_name);
        away = (TextView) v.findViewById(R.id.tv_away_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView homeRv = (RecyclerView) v.findViewById(R.id.rv_home);
        RecyclerView awayRv = (RecyclerView) v.findViewById(R.id.rv_away);

        homeRv.setAdapter(homeAdapter);
        awayRv.setAdapter(awayAdapter);
        homeRv.setLayoutManager(linearLayoutManager);
        awayRv.setLayoutManager(linearLayoutManager2);
        homeRv.setNestedScrollingEnabled(false);
        awayRv.setNestedScrollingEnabled(false);

        final FloatingActionButton fab = (FloatingActionButton) inflater.inflate(fit.cvut.org.cz.tmlibrary.R.layout.fab_add, ((ViewGroup) v), false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTeamDialog d = SelectTeamDialog.newInstance(homeParticipant.getParticipantId(), awayParticipant.getParticipantId(), homeName, awayName, getArguments().getLong(ARG_ID));
                d.setTargetFragment(MatchPlayersFragment.this, 0);
                d.show(getFragmentManager(), "select_team_tag");
            }
        });
        ((CoordinatorLayout) v).addView(fab);

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.sv);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fab.hide();
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    fab.show();
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            askForData = savedInstanceState.getBoolean(SAVE_DATA);
            homePlayers = savedInstanceState.getParcelableArrayList(SAVE_HOME_PLAYERS);
            awayPlayers = savedInstanceState.getParcelableArrayList(SAVE_AWAY_PLAYERS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_HOME, home.getText().toString());
        outState.putString(SAVE_AWAY, away.getText().toString());
        outState.putParcelableArrayList(SAVE_HOME_PLAYERS, new ArrayList<>(homeAdapter.getData()));
        outState.putParcelableArrayList(SAVE_AWAY_PLAYERS, new ArrayList<>(awayAdapter.getData()));
        outState.putBoolean(SAVE_DATA, askForData);
    }

    @Override
    public void customOnResume() {
        if (askForData)
            super.customOnResume();
        else {
            home.setText(homeName);
            away.setText(awayName);
            homeAdapter.swapData(homePlayers);
            awayAdapter.swapData(awayPlayers);
        }
    }

    @Override
    protected void customOnPause() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AddPlayersActivity.RESULT_OK) return;

        List<Player> players = data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA);
        List<PlayerStat> playerStats = new ArrayList<>();

        long participantId = requestCode == 0 ? homeParticipant.getId() : awayParticipant.getId();
        for (Player player : players) {
            PlayerStat playerStat = new PlayerStat(participantId, player.getId());
            playerStat.setName(player.getName());
            playerStats.add(playerStat);
        }

        if (requestCode == 0)
            homePlayers.addAll(playerStats);
        else
            awayPlayers.addAll(playerStats);
    }

    public List<PlayerStat> getHomePlayers() {return homeAdapter.getData();}
    public List<PlayerStat> getAwayPlayers() {return awayAdapter.getData();}

    public ArrayList<Player> getOmitPlayers() {
        ArrayList<Player> res = new ArrayList<>();
        for (PlayerStat playerStat : homeAdapter.getData())
            res.add(new Player(playerStat.getPlayerId(), playerStat.getName(), null, null));

        for (PlayerStat playerStat : awayAdapter.getData())
            res.add(new Player(playerStat.getPlayerId(), playerStat.getName(), null, null));

        return res;
    }
}
