package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.SquashInsertTeamDialog;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TeamAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamsListFragment extends AbstractListFragment<Team> {

    public static final String ARG_ID = "arg_id";

    public static TeamsListFragment newInstance(long tournamentId){
        TeamsListFragment fragment = new TeamsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {

        return new TeamAdapter(){
            @Override
            protected void setOnClickListeners(View v, long teamId) {
                final long ftid = teamId;

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        EditDeleteDialog dialog = new EditDeleteDialog(){
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(ftid, false, SquashInsertTeamDialog.class);
                                                insertTeamDialog.show(getFragmentManager(), "tag2");
                                                dialog.dismiss();
                                                break;
                                            default:break;
                                        }
                                    }
                                };
                            }
                        };
                        dialog.show(getFragmentManager(), "uberTag");


                        return true;
                    }
                });

            }
        };
    }

    @Override
    protected String getDataKey() {
        return TeamService.EXTRA_TEAMS;
    }

    @Override
    protected void askForData() {
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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
}
