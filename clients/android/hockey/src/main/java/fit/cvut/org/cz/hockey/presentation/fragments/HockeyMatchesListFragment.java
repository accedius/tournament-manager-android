package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.HockeyScoredMatch;
import fit.cvut.org.cz.hockey.presentation.activities.ShowMatchActivity;
import fit.cvut.org.cz.hockey.presentation.activities.ShowTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.adapters.HockeyScoredMatchAdapter;
import fit.cvut.org.cz.hockey.presentation.dialogs.AddMatchDialog;
import fit.cvut.org.cz.hockey.presentation.dialogs.EditDeleteResetDialog;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment for list of matches
 * Created by atgot_000 on 17. 4. 2016.
 */
public class HockeyMatchesListFragment extends AbstractListFragment<HockeyScoredMatch> {

    private long tournamentID;
    private static String ARG_ID = "tournament_id";

    private MatchReceiver matchReceiver = new MatchReceiver();


    public static HockeyMatchesListFragment newInstance( long id ) {
        HockeyMatchesListFragment fragment = new HockeyMatchesListFragment();
        Bundle args = new Bundle();

        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if( getArguments() != null ) {
            tournamentID = getArguments().getLong( ARG_ID, -1 );
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new HockeyScoredMatchAdapter(getResources()) {
            @Override
            protected void setOnClickListeners(View v, HockeyScoredMatch match, int position) {
                super.setOnClickListeners(v, match, position);
                final long fmId = match.getScoredMatch().getId();

                v.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = ShowMatchActivity.newStartIntent( getContext(), fmId );
                        startActivity( intent );
                    }
                });
                v.setOnLongClickListener( new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        EditDeleteResetDialog dialog = EditDeleteResetDialog.newInstance(fmId, tournamentID);
                        dialog.show(getFragmentManager(), "Edit_Delete_Reset_Dialog");
                        return true;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return MatchService.EXTRA_MATCH_LIST;
    }

    @Override
    public void askForData() {
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
        IntentFilter filter = new IntentFilter( MatchService.ACTION_FIND_BY_TOURNAMENT_ID);
        filter.addAction( MatchService.ACTION_CREATE);
        filter.addAction( MatchService.ACTION_GENERATE_ROUND);
        filter.addAction( MatchService.ACTION_DELETE);
        filter.addAction( MatchService.ACTION_RESTART);
        filter.addAction( MatchService.ACTION_UPDATE);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver( matchReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(matchReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final long tourId = getArguments().getLong(ARG_ID, -1);

               if( !((ShowTournamentActivity)getActivity()).isEnoughTeams() ){
                   Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.not_enough_teams, Snackbar.LENGTH_LONG).show();
                   return;
               }

               AddMatchDialog dialog = AddMatchDialog.newInstance(tourId);
               dialog.show(getFragmentManager(), "Add Match");
            }
        });

        return fab;
    }

    public class MatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case MatchService.ACTION_FIND_BY_TOURNAMENT_ID:
                    bindDataOnView(intent);
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    break;

                case MatchService.ACTION_GENERATE_ROUND:
                case MatchService.ACTION_CREATE:
                case MatchService.ACTION_UPDATE_FOR_OVERVIEW:
                case MatchService.ACTION_DELETE:
                case MatchService.ACTION_RESTART:
                case MatchService.ACTION_UPDATE:
                    contentView.setVisibility( View.GONE );
                    progressBar.setVisibility(View.VISIBLE);
                    askForData();
            }
        }
    }
}
