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

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.adapters.SimplePlayerAdapter;
import fit.cvut.org.cz.squash.presentation.dialogs.SelectTeamDialog;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchPlayersFragmentImproved extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";
    private static final String SAVE_HOME = "save_home";
    private static final String SAVE_AWAY = "save_away";
    private static final String SAVE_DATA = "save_data";
    public SimplePlayerAdapter homeAdapter = null, awayAdapter = null;
    private TextView homeName = null, awayName = null;
    private Team homeTeam = null, awayTeam = null;
    private boolean askForData = true;

    public static MatchPlayersFragmentImproved newInstance(long id){
        MatchPlayersFragmentImproved f = new MatchPlayersFragmentImproved();
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
        return PlayerService.isWorking(PlayerService.ACTION_GET_PLAYERS_FOR_MATCH);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        askForData = false;
        unregisterReceivers();
        homeTeam = intent.getParcelableExtra(PlayerService.EXTRA_HOME_PLAYERS);
        awayTeam = intent.getParcelableExtra(PlayerService.EXTRA_AWAY_PLAYERS);
        homeName.setText(homeTeam.getName());
        awayName.setText(awayTeam.getName());
        homeAdapter.swapData(homeTeam.getPlayers());
        awayAdapter.swapData(awayTeam.getPlayers());
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

        homeAdapter = new SimplePlayerAdapter();
        awayAdapter = new SimplePlayerAdapter();
        homeName = (TextView) v.findViewById(R.id.tv_home_name);
        awayName = (TextView) v.findViewById(R.id.tv_away_name);

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
                SelectTeamDialog d = new SelectTeamDialog(){
                    @Override
                    protected void homeClick() {
                        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TEAM, homeTeam.getId());
                        intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, homeAdapter.getData());
                        MatchPlayersFragmentImproved.this.startActivityForResult(intent, 0);
                    }

                    @Override
                    protected void awayClick() {
                        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TEAM, awayTeam.getId());
                        intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, awayAdapter.getData());
                        MatchPlayersFragmentImproved.this.startActivityForResult(intent, 1);
                    }

                    @Override
                    protected String getHomeName() {
                        return homeTeam.getName();
                    }

                    @Override
                    protected String getAwayName() {
                        return awayTeam.getName();
                    }
                };
                d.setRetainInstance(true);
                d.show(getFragmentManager(), "select_team_tag");
            }
        });
        ((CoordinatorLayout) v).addView(fab);

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.sv);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
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

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            askForData = savedInstanceState.getBoolean(SAVE_DATA);
            homeTeam = savedInstanceState.getParcelable(SAVE_HOME);
            awayTeam = savedInstanceState.getParcelable(SAVE_AWAY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (homeTeam != null)
            homeTeam.setPlayers(homeAdapter.getData());
        outState.putParcelable(SAVE_HOME, homeTeam);
        if (awayTeam != null)
            awayTeam.setPlayers(awayAdapter.getData());
        outState.putParcelable(SAVE_AWAY, awayTeam);
        outState.putBoolean(SAVE_DATA, askForData);
    }

    @Override
    public void customOnResume() {
        if (askForData)
            super.customOnResume();
        else {
            homeName.setText(homeTeam.getName());
            awayName.setText(awayTeam.getName());
            homeAdapter.swapData(homeTeam.getPlayers());
            awayAdapter.swapData(awayTeam.getPlayers());
        }
    }

    @Override
    protected void customOnPause() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AddPlayersActivity.RESULT_OK) return;

        ArrayList<Player> players = data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA);
        switch (requestCode){
            case 0:
                //hometeam
                if (homeTeam != null)
                    homeTeam.getPlayers().addAll(players);
                break;
            case 1:
                if (awayTeam != null)
                    awayTeam.getPlayers().addAll(players);
                break;
        }

    }

    public ArrayList<Player> getHomePlayers() {return homeAdapter.getData();}
    public ArrayList<Player> getAwayPlayers() {return awayAdapter.getData();}
}
