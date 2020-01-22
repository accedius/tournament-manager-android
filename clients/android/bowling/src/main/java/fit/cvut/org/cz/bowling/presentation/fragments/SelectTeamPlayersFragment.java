package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.adapters.SelectTeamPlayersAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class SelectTeamPlayersFragment extends AbstractSelectableListFragment<Player> {
    List<PlayerStat> alreadySelectedPlayers;
    long teamId;

    public static SelectTeamPlayersFragment newInstance(long teamId, List<PlayerStat> alreadySelectedPlayers) {
        SelectTeamPlayersFragment fragment = new SelectTeamPlayersFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ExtraConstants.EXTRA_TEAM_ID, teamId);
        bundle.putParcelableArrayList(ExtraConstants.EXTRA_SELECTED, (ArrayList<? extends Parcelable>) alreadySelectedPlayers);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamId = getArguments().getLong(ExtraConstants.EXTRA_TEAM_ID);
        alreadySelectedPlayers = getArguments().getParcelableArrayList(ExtraConstants.EXTRA_SELECTED);
    }

    @Override
    protected AbstractSelectableListAdapter<Player, ? extends OneActionViewHolder> getAdapter() {
        return new SelectTeamPlayersAdapter();
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TeamService.isWorking(TeamService.ACTION_GET_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(TeamService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }

    @Override
    public void askForData() {
        Intent intent = TeamService.newStartIntent(TeamService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, teamId);
        getContext().startService(intent);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Team team = intent.getParcelableExtra(ExtraConstants.EXTRA_TEAM);
        ArrayList<Player> allPlayersRegisteredInTeam = (ArrayList<Player>) team.getPlayers();
        ArrayList<Integer> alreadyInMatch = new ArrayList<>();
        int position = 0;
        for(Player player : allPlayersRegisteredInTeam) {
            for(PlayerStat stat : alreadySelectedPlayers) {
                if(player.getId() == stat.getPlayerId()) {
                    alreadyInMatch.add(position);
                    break;
                }
            }
            ++position;
        }

        intent.putExtra(getDataKey(), allPlayersRegisteredInTeam);
        intent.putExtra(getDataSelectedKey(), alreadyInMatch);
        super.bindDataOnView(intent);
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_PLAYERS;
    }

    @Override
    protected String getDataSelectedKey() {
        return ExtraConstants.EXTRA_SELECTED;
    }
}
