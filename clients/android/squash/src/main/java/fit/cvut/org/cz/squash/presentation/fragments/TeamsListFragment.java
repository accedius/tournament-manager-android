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
import fit.cvut.org.cz.squash.presentation.activities.TeamDetailActivity;
import fit.cvut.org.cz.squash.presentation.dialogs.SquashInsertTeamDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.TeamsDialog;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TeamAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Allows usser to display teams in tournament
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamsListFragment extends AbstractListFragment<Team> {

    private TeamAdapter adapter = null;

    public static final String ARG_ID = "arg_id";
    private BroadcastReceiver tReceiver = new TeamsReceiver();

    public static TeamsListFragment newInstance(long tournamentId){
        TeamsListFragment fragment = new TeamsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter =  new TeamAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long teamId, final int position, final String name) {
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        TeamsDialog dialog = TeamsDialog.newInstance(teamId, position, name);
                        dialog.show(getFragmentManager(), "uberTag");
                        return true;
                    }
                });
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = TeamDetailActivity.newStartIntent(teamId, getContext());
                        startActivity(intent);
                    }
                });
            }

        };
        return adapter;
    }

    @Override
    protected String getDataKey() {
        return TeamService.EXTRA_TEAMS;
    }

    @Override
    public void askForData() {
        Intent intent = TeamService.newStartIntent(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT, getContext());
        intent.putExtra(TeamService.EXTRA_ID, getArguments().getLong(ARG_ID,-1));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(TeamService.ACTION_DELETE);
        filter.addAction(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT);
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
                InsertTeamDialog dialog = SquashInsertTeamDialog.newInstance(getArguments().getLong(ARG_ID), true, SquashInsertTeamDialog.class);
                dialog.show(getFragmentManager(), "dialog");
            }

        });

        return fab;
    }

    public class TeamsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            if (intent.getAction().equals(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT)){
                TeamsListFragment.super.bindDataOnView(intent);
            } else {
                if (intent.getBooleanExtra(TournamentService.EXTRA_RESULT, false)){
                    int position = intent.getIntExtra(TournamentService.EXTRA_POSITION, -1);
                    adapter.delete(position);
                }
                else Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.failDeleteTeam, Snackbar.LENGTH_LONG).show();
            }

        }
    }
}
