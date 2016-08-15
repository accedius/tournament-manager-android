package fit.cvut.org.cz.squash.presentation.fragments;

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

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.activities.TournamentDetailActivity;
import fit.cvut.org.cz.squash.presentation.dialogs.TournamentsDialog;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Allows usser to display tournaments in competition
 * Created by Vaclav on 5. 4. 2016.
 */
public class TournamentsListFragment extends AbstractListFragment<Tournament> {

    public static final String COMP_ID = "comp_id";
    private CompetitionType type = null;
    private TournamentAdapter adapter = null;

    public static TournamentsListFragment newInstance(long competitionId){
        TournamentsListFragment fragment = new TournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(COMP_ID, competitionId);
        fragment.setArguments(args);
        return fragment;
    }

    public TournamentsListFragment() {}

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter =  new TournamentAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long tournamentId, final int position) {
                final Context c = getContext();

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(c, TournamentDetailActivity.class);
                        intent.putExtra(TournamentDetailActivity.EXTRA_ID, tournamentId);
                        intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);
                        intent.putExtra(TournamentDetailActivity.EXTRA_TYPE, type);

                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        TournamentsDialog dialog = TournamentsDialog.newInstance(getArguments().getLong(COMP_ID), tournamentId, position);
                        dialog.setTargetFragment(TournamentsListFragment.this, 0);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");

                        return false;
                    }
                });
            }
        };

        return adapter;
    }

    @Override
    protected String getDataKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_BY_COMPETITION_ID, getContext());
        if (getArguments() != null) intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(COMP_ID, -1));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_BY_COMPETITION_ID);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(TournamentService.ACTION_GET_BY_COMPETITION_ID);
        filter.addAction(TournamentService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(tReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(tReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cId = getArguments().getLong(COMP_ID, -1);
                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), cId, -1);
                startActivity(intent);
            }
        });

        return fab;
    }
    private BroadcastReceiver tReceiver = new TournamentsReceiver();
    public class TournamentsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            if (intent.getAction().equals(TournamentService.ACTION_GET_BY_COMPETITION_ID)){
                TournamentsListFragment.super.bindDataOnView(intent);
                type = CompetitionType.valueOf(intent.getStringExtra(TournamentService.EXTRA_TYPE));
            } else {
                if (intent.getBooleanExtra(TournamentService.EXTRA_RESULT, false)){
                    int position = intent.getIntExtra(TournamentService.EXTRA_POSITION, -1);
                    adapter.delete(position);
                }
                else Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.failDeleteTournament, Snackbar.LENGTH_LONG).show();
            }

        }
    }
}
