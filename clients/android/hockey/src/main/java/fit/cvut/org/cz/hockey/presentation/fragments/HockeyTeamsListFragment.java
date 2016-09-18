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
import fit.cvut.org.cz.hockey.presentation.activities.ShowTeamActivity;
import fit.cvut.org.cz.hockey.presentation.dialogs.HockeyInsertTeamDialog;
import fit.cvut.org.cz.hockey.presentation.dialogs.TeamsDialog;
import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TeamAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragmnet for list of teams in tournament
 * Created by atgot_000 on 17. 4. 2016.
 */
public class HockeyTeamsListFragment extends AbstractListFragment<Team> {

    public static final String TOUR_ID = "tour_id";
    public static final String COMP_ID = "comp_id";

    private BroadcastReceiver teamReceiver = new TeamReceiver();

    public static HockeyTeamsListFragment newInstance(long tournamentId, long competitionId){
        HockeyTeamsListFragment fragment = new HockeyTeamsListFragment();
        Bundle args = new Bundle();
        args.putLong(TOUR_ID, tournamentId);
        args.putLong(COMP_ID, competitionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new TeamAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long teamId, final int position, final String name) {
                super.setOnClickListeners(v, teamId, position, name);

                v.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent i = ShowTeamActivity.newStartIntent(getContext(), teamId);
                        startActivity(i);
                    }
                });

                v.setOnLongClickListener( new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v) {
                        TeamsDialog dialog = TeamsDialog.newInstance(teamId, position, name);
                        dialog.setTargetFragment(HockeyTeamsListFragment.this, 1);
                        dialog.show(getFragmentManager(), "tag3");
                        return true;
                    }
                });

            }
        };
    }

    @Override
    protected String getDataKey() {
        return TeamService.EXTRA_TEAM_LIST;
    }

    @Override
    public void askForData() {
        Intent intent = TeamService.newStartIntent(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT, getContext());
        intent.putExtra(TeamService.EXTRA_ID, getArguments().getLong(TOUR_ID, -1));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT);
        filter.addAction(TeamService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(teamReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(teamReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertTeamDialog dialog = InsertTeamDialog.newInstance(getArguments().getLong(TOUR_ID), true, HockeyInsertTeamDialog.class);
                dialog.setTargetFragment(HockeyTeamsListFragment.this, 0);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        return fab;
    }

    public class TeamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT: {
                    HockeyTeamsListFragment.super.bindDataOnView(intent);
                    break;
                }
                case TeamService.ACTION_DELETE: {
                    if (intent.getIntExtra( TeamService.EXTRA_OUTCOME, -1 ) == TeamService.OUTCOME_OK){
                        int position = intent.getIntExtra(TeamService.EXTRA_POSITION, -1);
                        adapter.delete(position);
                        break;
                    } else {
                        View v = getView();
                        if(v != null) Snackbar.make(v, R.string.team_not_empty_error, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    /**
     *
     * @return current number of teams in the list
     */
    public int teamCount() {
        return recyclerView.getAdapter().getItemCount();
    }
}
