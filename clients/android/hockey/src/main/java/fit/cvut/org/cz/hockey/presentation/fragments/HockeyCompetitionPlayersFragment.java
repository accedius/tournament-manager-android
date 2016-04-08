package fit.cvut.org.cz.hockey.presentation.fragments;

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
import fit.cvut.org.cz.hockey.business.managers.StatisticsManager;
import fit.cvut.org.cz.hockey.presentation.adapters.AgregatedStatisticsAdapter;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class HockeyCompetitionPlayersFragment extends AbstractListFragment<AgregatedStatistics> {

    private long competitionID;
    private static String ARG_ID = "competition_id";

    public static HockeyCompetitionPlayersFragment newInstance( long id )
    {
        HockeyCompetitionPlayersFragment fragment = new HockeyCompetitionPlayersFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments( args );
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if( getArguments() != null )
            competitionID = getArguments().getLong( ARG_ID );

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
        Intent intent = StatsService.newStartIntent( StatsService.ACTION_GET_BY_COMP_ID, getContext() );
        intent.putExtra(StatsService.EXTRA_ID, competitionID );
        getActivity().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking( StatsService.ACTION_GET_BY_COMP_ID );
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter(StatsService.ACTION_GET_BY_COMP_ID));
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

}
