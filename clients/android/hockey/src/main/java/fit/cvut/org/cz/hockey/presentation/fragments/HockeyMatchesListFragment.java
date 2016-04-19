package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ScoredMatchAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class HockeyMatchesListFragment extends AbstractListFragment<ScoredMatch> {

    private long tournamentID;
    private static String ARG_ID = "tournament_id";


    public static HockeyMatchesListFragment newInstance( long id )
    {
        HockeyMatchesListFragment fragment = new HockeyMatchesListFragment();
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
        return new ScoredMatchAdapter();
    }

    @Override
    protected String getDataKey() {
        return MatchService.EXTRA_MATCH_LIST;
    }

    @Override
    protected void askForData() {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_FIND_BY_TOURNAMENT_ID, getContext() );
        intent.putExtra( MatchService.EXTRA_TOUR_ID, tournamentID );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking( MatchService.ACTION_FIND_BY_TOURNAMENT_ID );
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter( MatchService.ACTION_FIND_BY_TOURNAMENT_ID ));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       long tourId = getArguments().getLong(ARG_ID, -1);
                                       Intent intent = CreateMatchActivity.newStartIntent(getContext(), tourId, true);

                                       startActivity(intent);
                                   }
                               }
        );

        return fab;
    }


}
