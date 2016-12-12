package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by kevin on 30.8.2016.
 */
public class SquashMatchOverviewFragment extends AbstractDataFragment {
    private View v;

    private SetsFragment sf;

    private static final String ARG_ID = "arg_id";
    private static final String ARG_PLAYED = "arg_played";

    private Match match = null;

    private Long tournament_id;

    public static SquashMatchOverviewFragment newInstance(long id, boolean played) {
        SquashMatchOverviewFragment fragment = new SquashMatchOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putBoolean(ARG_PLAYED, played);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GET_MATCH_DETAIL, getContext());
        intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_GET_MATCH_DETAIL);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        match = intent.getParcelableExtra(MatchService.EXTRA_MATCH);
        tournament_id = match.getTournamentId();

        getActivity().setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.match) + " – " +
                match.getHomeName() + " " +
                getResources().getString(R.string.vs) + " " +
                match.getAwayName());
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_GET_MATCH_DETAIL));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_sets, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getChildFragmentManager().findFragmentById(R.id.sets) == null) {
            sf = SetsFragment.newInstance(getArguments().getLong(ARG_ID), getArguments().getBoolean(ARG_PLAYED));
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.sets, sf)
                    .commit();
        }
    }

    public SetsFragment getSetsFragment() {
        return sf;
    }

    public Long getTournamentId() {
        return tournament_id;
    }
}
