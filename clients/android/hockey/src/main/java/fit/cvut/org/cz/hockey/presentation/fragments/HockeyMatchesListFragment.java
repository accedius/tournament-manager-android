package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.activities.ShowMatchActivity;
import fit.cvut.org.cz.hockey.presentation.dialogs.AddMatchDialog;
import fit.cvut.org.cz.hockey.presentation.dialogs.EditDeleteResetDialog;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
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

    private MatchReceiver matchReceiver = new MatchReceiver();


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
        return new ScoredMatchAdapter() {
            @Override
            protected void setOnClickListeners(View v, ScoredMatch match) {
                super.setOnClickListeners(v, match);
                final long fmId = match.getId();

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

                        EditDeleteResetDialog dialog = new EditDeleteResetDialog(){
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch ( which )
                                        {
                                            case 0:
                                            {
                                                Intent intent = CreateMatchActivity.newStartIntent( getContext(), fmId, tournamentID);
                                                startActivity( intent );

                                                break;
                                            }
                                            case 1:
                                            {
                                                Intent intent = MatchService.newStartIntent( MatchService.ACTION_DELETE, getContext() );
                                                intent.putExtra( MatchService.EXTRA_ID, fmId );

                                                getContext().startService( intent );
                                                break;
                                            }
                                            case 2:
                                            {
                                                Intent intent = MatchService.newStartIntent( MatchService.ACTION_RESTART, getContext() );
                                                intent.putExtra( MatchService.EXTRA_ID, fmId );

                                                getContext().startService( intent );
                                                break;
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };

                        dialog.show(getFragmentManager(), "Edit_Delete_Reset_Dialog");

                        return false;
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

                                       AddMatchDialog dialog = new AddMatchDialog(){
                                           @Override
                                           protected DialogInterface.OnClickListener supplyListener() {
                                               return new DialogInterface.OnClickListener(){
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       switch ( which )
                                                       {
                                                           case 0:
                                                           {
                                                               //TODO pridat kontrolu poctu tymu (musi byt aspon 2)
                                                               Intent intent = CreateMatchActivity.newStartIntent(getContext(), tourId);

                                                               startActivity(intent);
                                                               break;
                                                           }
                                                           case 1:
                                                           {
                                                               Intent intent = MatchService.newStartIntent( MatchService.ACTION_GENERATE_ROUND, getContext());
                                                               intent.putExtra( MatchService.EXTRA_TOUR_ID, tourId );

                                                               getContext().startService( intent );
                                                               break;
                                                           }
                                                       }
                                                       dialog.dismiss();
                                                   }
                                               };
                                           }
                                       };

                                       dialog.show(getFragmentManager(), "Add Match");


                                   }
                               }
        );

        return fab;
    }

    public class MatchReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action)
            {
                case MatchService.ACTION_FIND_BY_TOURNAMENT_ID: {
                    HockeyMatchesListFragment.super.bindDataOnView(intent);
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    break;
                }
                case MatchService.ACTION_GENERATE_ROUND:
                case MatchService.ACTION_CREATE:
                case MatchService.ACTION_UPDATE_FOR_OVERVIEW:
                case MatchService.ACTION_DELETE:
                case MatchService.ACTION_RESTART:
                case MatchService.ACTION_UPDATE:
                {
                    contentView.setVisibility( View.GONE );
                    progressBar.setVisibility(View.VISIBLE);
                    askForData();
                }
                default: break;

            }
        }
    }


}
