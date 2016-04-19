package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.presentation.adapters.StandingsAdapter;
import fit.cvut.org.cz.hockey.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsFragment extends AbstractListFragment<Standing> {

    private long tournamentID;
    private static String ARG_ID = "tournament_id";


    public static StandingsFragment newInstance( long id )
    {
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();

        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if( getArguments() != null )
        {
            tournamentID = getArguments().getLong( ARG_ID, -1 );
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new StandingsAdapter();
    }

    @Override
    protected String getDataKey() {
        return StatsService.EXTRA_STANDINGS;
    }

    @Override
    protected void askForData() {
        Intent intent = StatsService.newStartIntent( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT, getContext() );
        intent.putExtra( StatsService.EXTRA_ID, tournamentID );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter( StatsService.ACTION_GET_STANDINGS_BY_TOURNAMENT ));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
