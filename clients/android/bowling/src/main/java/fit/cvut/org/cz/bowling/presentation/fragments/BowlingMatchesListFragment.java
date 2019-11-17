package fit.cvut.org.cz.bowling.presentation.fragments;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.activities.ShowMatchActivity;
import fit.cvut.org.cz.bowling.presentation.activities.ShowTournamentActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.BowlingMatchAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.AddMatchDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.EditDeleteResetDialog;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment is used in
 */
public class BowlingMatchesListFragment extends AbstractListFragment<Match> {
    private long tournamentId;
    private MatchReceiver matchReceiver = new MatchReceiver();

    public static BowlingMatchesListFragment newInstance(long id) {
        BowlingMatchesListFragment fragment = new BowlingMatchesListFragment();
        Bundle args = new Bundle();

        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new BowlingMatchAdapter(getResources()) {
            @Override
            protected void setOnClickListeners(View v, Match match, int position, final String title) {
                super.setOnClickListeners(v, match, position, title);
                final long fmId = match.getId();

                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = ShowMatchActivity.newStartIntent(getContext(), fmId);
                        startActivity(intent);
                    }
                });
                v.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        EditDeleteResetDialog dialog = EditDeleteResetDialog.newInstance(fmId, tournamentId, title);
                        dialog.show(getFragmentManager(), "Edit_Delete_Reset_Dialog");
                        return true;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_MATCHES;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_FIND_BY_TOURNAMENT_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_FIND_BY_TOURNAMENT_ID);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(MatchService.ACTION_FIND_BY_TOURNAMENT_ID);
        filter.addAction(MatchService.ACTION_CREATE);
        filter.addAction(MatchService.ACTION_GENERATE_ROUND);
        filter.addAction(MatchService.ACTION_DELETE);
        filter.addAction(MatchService.ACTION_RESET);
        filter.addAction(MatchService.ACTION_UPDATE);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(matchReceiver, filter);
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
                final long tourId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);

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
                case MatchService.ACTION_RESET:
                case MatchService.ACTION_UPDATE:
                    contentView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    askForData();
            }
        }
    }
}