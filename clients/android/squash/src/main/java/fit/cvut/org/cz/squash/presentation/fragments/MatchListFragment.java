package fit.cvut.org.cz.squash.presentation.fragments;

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

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.squash.presentation.activities.MatchDetailActivity;
import fit.cvut.org.cz.squash.presentation.dialogs.AddMatchDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.MatchesDialog;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.MatchAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Displays list matches in tournament
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchListFragment extends AbstractListFragment<Match> {
    public static final String ARG_ID = "arg_id";
    private CompetitionType type = null;
    private MatchAdapter adapter = null;

    public static MatchListFragment newInstance(long tournamentId){
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter = new MatchAdapter(getResources()){
            @Override
            protected void setOnClickListeners(View v, final Match match, final int position, final String title) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = MatchDetailActivity.newStartIntent(getContext(), match.getId(), match.isPlayed(), type);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        MatchesDialog dialog = MatchesDialog.newInstance(match.getId(), match.getTournamentId(), position, title);
                        dialog.show(getFragmentManager(), "EDIT_DELETE_RESET");
                        return false;
                    }
                });
            }
        };

        return adapter;
    }

    @Override
    protected String getDataKey() {
        return MatchService.EXTRA_MATCHES;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GET_MATCHES_BY_TOURNAMENT, getContext());
        intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID, -1));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_GET_MATCHES_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(MatchService.ACTION_GET_MATCHES_BY_TOURNAMENT);
        filter.addAction(MatchService.ACTION_GENERATE_ROUND);
        filter.addAction(MatchService.ACTION_CAN_ADD_MATCH);
        filter.addAction(MatchService.ACTION_DELETE_MATCH);
        filter.addAction(MatchService.ACTION_RESET_MATCH);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(tReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(tReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);
        final long id = getArguments().getLong(ARG_ID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMatchDialog d = AddMatchDialog.newInstance(id);
                d.setTargetFragment(MatchListFragment.this, 0);
                d.show(getFragmentManager(), "Add_match_dialog");
            }
        });
        return fab;
    }

    private BroadcastReceiver tReceiver = new MatchReceiver();
    public class MatchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MatchService.ACTION_CAN_ADD_MATCH:{
                    if (intent.getBooleanExtra(MatchService.EXTRA_RESULT, false)) {
                        Intent start = CreateMatchActivity.newStartIntent(getContext(), -1, getArguments().getLong(ARG_ID));
                        startActivity(start);
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);
                        Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.cant_create_match, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                }
                case MatchService.ACTION_GENERATE_ROUND:{
                    if (intent.getBooleanExtra(MatchService.EXTRA_RESULT, false)) {
                        askForData();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);
                        Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.cant_create_match, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                }
                case MatchService.ACTION_DELETE_MATCH:{
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    adapter.delete(intent.getIntExtra(MatchService.EXTRA_POSITION, -1));
                    break;
                }
                case MatchService.ACTION_RESET_MATCH:{
                    customOnResume();
                    break;
                }
                default:{
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    MatchListFragment.super.bindDataOnView(intent);
                    type = CompetitionTypes.competitionTypes()[intent.getIntExtra(MatchService.EXTRA_TYPE, 0)];
                }
            }
        }
    }
}
