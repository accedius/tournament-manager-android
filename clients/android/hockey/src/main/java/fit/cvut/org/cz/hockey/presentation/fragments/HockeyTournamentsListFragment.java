package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.activities.ShowTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment for list of tournaments in competition
 * Created by atgot_000 on 29. 3. 2016.
 */
public class HockeyTournamentsListFragment extends AbstractListFragment<Tournament> {

    private long competitionId;
    private static String ARG_ID = "competition_id";

    private TournamentsListReceiver myReceiver = new TournamentsListReceiver();

    public static HockeyTournamentsListFragment newInstance(long id) {
        HockeyTournamentsListFragment fragment = new HockeyTournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null)
            competitionId = getArguments().getLong(ARG_ID);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new TournamentAdapter(){
            @Override
            protected void setOnClickListeners(View v, long tournamentId, final int position, final String name) {
                final long compId = competitionId;
                final long tourId = tournamentId;

                v.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Intent intent = new Intent(getContext(), ShowTournamentActivity.class);
                             Bundle b = new Bundle();
                             b.putLong(ShowTournamentActivity.COMP_ID, compId);
                             b.putLong(ShowTournamentActivity.TOUR_ID, tourId);
                             intent.putExtras(b);
                             intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);
                             startActivity(intent);
                         }
                     }
                );
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        EditDeleteDialog dialog = EditDeleteDialog.newInstance(tourId, compId, position, name);
                        dialog.setTargetFragment(HockeyTournamentsListFragment.this, 1);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");
                        return true;
                    }
                } );
                super.setOnClickListeners(v, tournamentId, position, name);
            }
        };
    }

    @Override
    protected String getDataKey() {
        return TournamentService.EXTRA_LIST;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_ALL, getContext());
        intent.putExtra(TournamentService.EXTRA_COMP_ID, competitionId);
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter( TournamentService.ACTION_GET_ALL);
        filter.addAction( TournamentService.ACTION_DELETE );
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   long compId = getArguments().getLong(ARG_ID, -1);
                   Intent intent = CreateTournamentActivity.newStartIntent(getContext(), -1, compId);
                   startActivity(intent);
               }
           }
        );

        return fab;
    }

    public class TournamentsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            switch (action) {
                case TournamentService.ACTION_GET_ALL: {
                    HockeyTournamentsListFragment.super.bindDataOnView(intent);
                    break;
                }
                case TournamentService.ACTION_DELETE: {
                    int result = intent.getIntExtra(TournamentService.EXTRA_RESULT, -1);
                    if (result == 0) {
                        int position = intent.getIntExtra(TournamentService.EXTRA_POSITION, -1);
                        adapter.delete(position);
                    } else {
                        View v = getView();
                        if( v != null )
                            Snackbar.make(v, R.string.tournament_not_empty_error, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }
}
