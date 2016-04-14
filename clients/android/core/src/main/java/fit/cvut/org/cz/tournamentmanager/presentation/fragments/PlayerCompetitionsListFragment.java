package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class PlayerCompetitionsListFragment extends AbstractListFragment<Competition> {

    private long playerID;
    private static String ARG_ID = "player_id";

    public static PlayerCompetitionsListFragment newInstance(long id) {
        PlayerCompetitionsListFragment fragment = new PlayerCompetitionsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null)
            playerID = getArguments().getLong(ARG_ID);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return null;/*
        return new CompetitionAdapter(
                "fit.cvut.org.cz.hockey",
                "fit.cvut.org.cz.hockey.presentation.activites.ShowCompetitionActivity",
                getActivity()
        );*/
    }

    @Override
    protected String getDataKey() {
        return CompetitionService.EXTRA_LIST;
    }

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.getStartIntent(CompetitionService.ACTION_GET_ALL, "fit.cvut.org.cz.hockey", getContext());
        intent.putExtra(CompetitionService.EXTRA_PLAYER_ID, playerID);
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_GET_ALL));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
