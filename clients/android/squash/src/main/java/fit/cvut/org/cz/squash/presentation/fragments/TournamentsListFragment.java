package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.activities.TournamentDetailActivity;
import fit.cvut.org.cz.squash.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class TournamentsListFragment extends AbstractListFragment<Tournament> {

    public static final String ARG_ID = "arg_id";

    public static TournamentsListFragment newInstance(long competitionId){
        TournamentsListFragment fragment = new TournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, competitionId);
        fragment.setArguments(args);
        return fragment;
    }

    public TournamentsListFragment() {}

    @Override
    protected AbstractListAdapter getAdapter() {
        return new TournamentAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long tournamentId) {
                final Context c = getContext();

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(c, TournamentDetailActivity.class);
                        intent.putExtra(TournamentDetailActivity.EXTRA_ID, tournamentId);
                        intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);

                        //TODO navazat typ

                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        final View fw =v;

                        EditDeleteDialog dialog = new EditDeleteDialog() {
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:{
                                                Intent intent = CreateTournamentActivity.newStartIntent(c, tournamentId, false);
                                                startActivity(intent);

                                                break;
                                            }
                                            case 1:{
                                                //TODO Delete not implemented yet
                                                Snackbar.make(fw, "delete not yet implemented", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };

                        dialog.show(getFragmentManager(), "EDIT_DELETE");

                        return false;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    protected void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_BY_COMPETITION_ID, getContext());
        if (getArguments() != null) intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(ARG_ID, -1));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_BY_COMPETITION_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_GET_BY_COMPETITION_ID));
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
                long cId = getArguments().getLong(ARG_ID, -1);
                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), cId, true);
                startActivity(intent);
            }
        });

        return fab;
    }
}
