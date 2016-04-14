package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.hockey.presentation.adapters.AgregatedStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
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

    public static HockeyPlayersStatsFragment newInstance( long id, boolean forComp )
    {
        HockeyPlayersStatsFragment fragment = new HockeyPlayersStatsFragment();
        Bundle args = new Bundle();

        if( forComp ) args.putLong(ARG_COMP_ID, id);
        else args.putLong(ARG_TOUR_ID, id);

        fragment.setArguments( args );
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if( getArguments() != null ) {
            competitionID = getArguments().getLong(ARG_COMP_ID, -1);
            tournamentID = getArguments().getLong(ARG_TOUR_ID, -1);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
        
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
        }
        else {
            filter = new IntentFilter(StatsService.ACTION_GET_BY_TOUR_ID);
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance( getContext() ).unregisterReceiver(receiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false );

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                   }
                               }
        );

        return fab;
    }

//    public class StatsReceiver extends BroadcastReceiver
//    {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            switch (action)
//            {
//                case StatsService.ACTION_GET_BY_TOUR_ID:
//                case StatsService.ACTION_GET_BY_COMP_ID:
//
//            }
//        }
//    }

}
