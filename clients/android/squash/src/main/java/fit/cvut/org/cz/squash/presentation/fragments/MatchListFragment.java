package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ScoredMatchAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchListFragment extends AbstractListFragment<ScoredMatch> {

    public static final String ARG_ID = "arg_id";

    public static MatchListFragment newInstance(long tournamentId){
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new ScoredMatchAdapter();
    }

    @Override
    protected String getDataKey() {
        return MatchService.EXTRA_MATCHES;
    }

    @Override
    protected void askForData() {
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_GET_MATCHES_BY_TOURNAMENT));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
