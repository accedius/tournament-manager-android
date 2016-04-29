package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.hockey.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.hockey.presentation.adapters.AgregatedStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.dialogs.DeleteOnlyDialog;
import fit.cvut.org.cz.hockey.presentation.services.PlayerService;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class HockeyPlayersStatsFragment extends AbstractListFragment<AgregatedStatistics> {

    private long competitionID;
    private long tournamentID;
    private static String ARG_COMP_ID = "competition_id";
    private static String ARG_TOUR_ID = "tournament_id";
    public static final String SAVE_COMP_ID = "SAVE_COMP_ID";
    public static final String SAVE_TOUR_ID = "SAVE_TOUR_ID";
    public static final String SAVE_SEND = "SAVE_SEND";

    private boolean sendForData = true;

    private BroadcastReceiver statsReceiver = new StatsReceiver();

    public static HockeyPlayersStatsFragment newInstance( long id, boolean forComp )
    {
        HockeyPlayersStatsFragment fragment = new HockeyPlayersStatsFragment();
        Bundle args = new Bundle();

        if( forComp ) args.putLong(ARG_COMP_ID, id);
        else args.putLong(ARG_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SEND, sendForData);
        outState.putLong(SAVE_COMP_ID, competitionID);
        outState.putLong(SAVE_TOUR_ID, tournamentID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            tournamentID = savedInstanceState.getLong(SAVE_TOUR_ID);
            competitionID = savedInstanceState.getLong(SAVE_COMP_ID);
            sendForData = savedInstanceState.getBoolean(SAVE_SEND);
        }

        if( getArguments() != null ) {
            competitionID = getArguments().getLong(ARG_COMP_ID, -1);
            tournamentID = getArguments().getLong(ARG_TOUR_ID, -1);
        }
    }


    @Override
    protected AbstractListAdapter getAdapter() {
        return new AgregatedStatisticsAdapter(){
            @Override
            protected void setOnClickListeners(View v, long playerId) {

                final long final_plId = playerId;

                super.setOnClickListeners(v, playerId);
                v.setOnLongClickListener( new View.OnLongClickListener(){

                    @Override
                    public boolean onLongClick(View v) {
                        DeleteOnlyDialog dialog = new DeleteOnlyDialog(){
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch ( which )
                                        {
                                            case 0:
                                            {

                                                if(competitionID != -1){
                                                    Intent intent = PlayerService.newStartIntent( PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION, getContext());
                                                    intent.putExtra( PlayerService.EXTRA_PLAYER_ID, final_plId );
                                                    intent.putExtra( PlayerService.EXTRA_ID, competitionID );
                                                    getContext().startService( intent );
                                                } else {
                                                    Intent intent = PlayerService.newStartIntent( PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT, getContext());
                                                    intent.putExtra( PlayerService.EXTRA_PLAYER_ID, final_plId );
                                                    intent.putExtra( PlayerService.EXTRA_ID, tournamentID );
                                                    getContext().startService( intent );
                                                }
                                                break;
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };
                        dialog.show(getFragmentManager(), "EDIT_DELETE");

                        return false;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STATS;
    }

    @Override
    protected void askForData() {
        if( !sendForData ) return;

        Intent intent;
        if( competitionID != -1 ) {
            intent = StatsService.newStartIntent(StatsService.ACTION_GET_BY_COMP_ID, getContext());
            intent.putExtra(StatsService.EXTRA_ID, competitionID );
        }
        else {
            intent = StatsService.newStartIntent( StatsService.ACTION_GET_BY_TOUR_ID, getContext() );
            intent.putExtra(StatsService.EXTRA_ID, tournamentID );
        }

        getActivity().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {

        if( competitionID != -1 ) {
            return StatsService.isWorking( StatsService.ACTION_GET_BY_COMP_ID );
        }
        else {
            return StatsService.isWorking( StatsService.ACTION_GET_BY_TOUR_ID );
        }
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter;

        if( competitionID != -1 ) {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_COMP_ID);
            filter.addAction( PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION );
            filter.addAction( PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION );
        }
        else {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_TOUR_ID);
            filter.addAction( PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT );
            filter.addAction( PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT );
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver( statsReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(statsReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false );

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                       int requestCode;
                                       int option;
                                       long id;
                                       sendForData = false;
                                       if (competitionID != -1) {
                                           option = AddPlayersFragment.OPTION_COMPETITION;
                                           id = competitionID;
                                           requestCode = option;
                                       } else {
                                           option = AddPlayersFragment.OPTION_TOURNAMENT;
                                           id = tournamentID;
                                           requestCode = option;
                                       }

                                       Intent intent = AddPlayersActivity.newStartIntent(getContext(), option, id);

                                       startActivityForResult(intent, requestCode);
                                   }
                               }
        );

        return fab;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != SelectableListActivity.RESULT_OK){
            sendForData = true;
            askForData();
            return;
        }

        if( competitionID != -1 ) {
            Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION, getContext());
            intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA));
            intent.putExtra(PlayerService.EXTRA_ID, competitionID);
            getContext().startService(intent);
        }
        else {
            Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT, getContext());
            intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, data.getParcelableArrayListExtra(AddPlayersActivity.EXTRA_DATA));
            intent.putExtra(PlayerService.EXTRA_ID, tournamentID);
            getContext().startService(intent);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    public class StatsReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            contentView.setVisibility(View.VISIBLE);
            switch (action)
            {
                case StatsService.ACTION_GET_BY_TOUR_ID:
                case StatsService.ACTION_GET_BY_COMP_ID: {
                    HockeyPlayersStatsFragment.super.bindDataOnView(intent);
                    progressBar.setVisibility(View.GONE);
                    break;
                }
                case PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT:
                case PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION:
                {
                    sendForData = true;
                    askForData();
                    break;
                }
                case PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION:
                case PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT:
                {
                    if( intent.getIntExtra( PlayerService.EXTRA_OUTCOME, -1 ) == PlayerService.OUTCOME_OK ){
                        sendForData = true;
                        askForData();
                        break;
                    } else {
                        View v = getView();
                        if( v != null ) Snackbar.make(v, R.string.player_cant_delete, Snackbar.LENGTH_LONG).show();
                    }
                }
                default: break;

            }
        }
    }

}
