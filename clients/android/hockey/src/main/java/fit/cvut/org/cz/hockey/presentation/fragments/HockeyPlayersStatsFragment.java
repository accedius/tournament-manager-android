package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.hockey.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.hockey.presentation.adapters.AgregatedStatisticsAdapter;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( getArguments() != null ) {
            competitionID = getArguments().getLong(ARG_COMP_ID, -1);
            tournamentID = getArguments().getLong(ARG_TOUR_ID, -1);
        }
    }


    @Override
    protected AbstractListAdapter getAdapter() {
        return new AgregatedStatisticsAdapter();
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STATS;
    }

    @Override
    protected void askForData() {
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
        }
        else {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_TOUR_ID);
            filter.addAction( PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT );
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

        if (resultCode != SelectableListActivity.RESULT_OK) return;

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
        if( progressInterface != null )
            progressInterface.showProgress();
    }

    public class StatsReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String getAction;
            String addAction;

            if( competitionID != -1 ) {
                getAction = StatsService.ACTION_GET_BY_COMP_ID;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION;
            }
            else {
                getAction = StatsService.ACTION_GET_BY_TOUR_ID;
                addAction = PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT;
            }

            switch (action)
            {
                case StatsService.ACTION_GET_BY_TOUR_ID:
                case StatsService.ACTION_GET_BY_COMP_ID:
                    HockeyPlayersStatsFragment.super.bindDataOnView(intent);
                    if (!PlayerService.isWorking(addAction) && !StatsService.isWorking(getAction) && progressInterface != null)
                        progressInterface.hideProgress();
                    break;
                case PlayerService.ACTION_ADD_PLAYERS_TO_TOURNAMENT:
                case PlayerService.ACTION_ADD_PLAYERS_TO_COMPETITION:
                {
                    //TODO opravit refresh
//                    Intent startIntent = StatsService.newStartIntent(getAction, getContext());
//                    getContext().startService(startIntent);
                    break;
                }


            }
        }
    }

}
