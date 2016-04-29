package fit.cvut.org.cz.hockey.presentation.fragments;

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
import fit.cvut.org.cz.hockey.presentation.activities.ShowTeamActivity;
import fit.cvut.org.cz.hockey.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.hockey.presentation.dialogs.HockeyInsertTeamDialog;
import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TeamAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class HockeyTeamsListFragment extends AbstractListFragment<Team> {

    public static final String ARG_ID = "arg_id";

    public static HockeyTeamsListFragment newInstance(long tournamentId){
        HockeyTeamsListFragment fragment = new HockeyTeamsListFragment();
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
                super.setOnClickListeners(v, teamId);
                final long tid = teamId;

                v.setOnClickListener( new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent i = ShowTeamActivity.newStartIntent( getContext(), tid );
                        startActivity( i );
                    }
                });

                v.setOnLongClickListener( new View.OnLongClickListener(){

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
                                                InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(tid, false, HockeyInsertTeamDialog.class);
                                                insertTeamDialog.show(getFragmentManager(), "tag2");
                                                dialog.dismiss();
                                                break;
                                            case 1:
                                            {
                                                Intent intent = TeamService.newStartIntent( TeamService.ACTION_DELETE, getContext() );
                                                intent.putExtra( TeamService.EXTRA_ID, tid );
                                                getContext().startService( intent );
                                                dialog.dismiss();
                                                break;
                                            }
                                            default:break;
                                        }
                                    }
                                };
                            }
                        };
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
    protected void askForData() {
        Intent intent = TeamService.newStartIntent( TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT, getContext() );
        intent.putExtra( TeamService.EXTRA_ID, getArguments().getLong(ARG_ID, -1) );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking( TeamService.ACTION_GET_TEAMS_BY_TOURNAMENT );
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
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertTeamDialog dialog = InsertTeamDialog.newInstance(getArguments().getLong(ARG_ID), true, HockeyInsertTeamDialog.class);
                dialog.show(getFragmentManager(), "dialog");
            }

        });

        return fab;
    }
}
