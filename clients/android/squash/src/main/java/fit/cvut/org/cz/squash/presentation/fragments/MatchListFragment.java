package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import fit.cvut.org.cz.squash.presentation.dialogs.EditDeleteResetDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.NewMatchDialog;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ScoredMatchAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchListFragment extends AbstractListFragment<ScoredMatch> {

    public static final String ARG_ID = "arg_id";
    private CompetitionType type = null;
    private ScoredMatchAdapter adapter = null;

    public static MatchListFragment newInstance(long tournamentId){
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        final Context c = getContext();
        adapter = new ScoredMatchAdapter(){
            @Override
            protected void setOnClickListeners(View v, final ScoredMatch match, final int position) {
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
                        EditDeleteResetDialog dialog = new EditDeleteResetDialog() {
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:{
                                                Intent intent = CreateMatchActivity.newStartIntent(c, match.getId(), match.getTournamentId());
                                                startActivity(intent);
                                                break;
                                            }
                                            case 1:{
                                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_DELETE_MATCH, c);
                                                intent.putExtra(MatchService.EXTRA_ID, match.getId());
                                                c.startService(intent);
                                                adapter.delete(position);
                                                break;
                                            }
                                            case 2:{
                                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_RESET_MATCH, c);
                                                intent.putExtra(MatchService.EXTRA_ID, match.getId());
                                                c.startService(intent);
                                                customOnResume();
                                                break;
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };

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
                NewMatchDialog d = new NewMatchDialog() {
                    @Override
                    protected DialogInterface.OnClickListener supplyListener() {
                        return new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:{
                                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_CAN_ADD_MATCH, getContext());
                                        intent.putExtra(MatchService.EXTRA_ID, id);
                                        getContext().startService(intent);
                                        break;
                                    }
                                    case 1:{
                                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GENERATE_ROUND, getContext());
                                        intent.putExtra(MatchService.EXTRA_ID, id);
                                        getContext().startService(intent);
                                        break;
                                    }
                                }
                                progressBar.setVisibility(View.VISIBLE);
                                contentView.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        };
                    }
                };
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
                    if (intent.getBooleanExtra(MatchService.EXTRA_RESULT, false)){
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
                    if (intent.getBooleanExtra(MatchService.EXTRA_RESULT, false)){
                        askForData();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        contentView.setVisibility(View.VISIBLE);
                        Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.cant_create_match, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                }
                default:{
                    progressBar.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    MatchListFragment.super.bindDataOnView(intent);
                    type = CompetitionType.valueOf(intent.getStringExtra(MatchService.EXTRA_TYPE));
                }
            }
        }
    }
}
