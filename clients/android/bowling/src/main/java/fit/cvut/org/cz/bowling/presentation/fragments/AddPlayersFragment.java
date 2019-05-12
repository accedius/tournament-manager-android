package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.SelectPlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class AddPlayersFragment extends AbstractSelectableListFragment<Player> {
    public static final int OPTION_COMPETITION = 1;
    public static final int OPTION_TOURNAMENT = 2;
    public static final int OPTION_TEAM = 3;
    public static final int OPTION_PARTICIPANT = 4;

    private String action = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        int option = bundle.getInt(ExtraConstants.EXTRA_OPTION, -1);

        switch (option) {
            case OPTION_COMPETITION : {
                action = PlayerService.ACTION_GET_PLAYERS_NOT_IN_COMPETITION;
                break;
            }
            case OPTION_TOURNAMENT : {
                action = PlayerService.ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT;
                break;
            }
            case OPTION_TEAM: {
                action = PlayerService.ACTION_GET_PLAYERS_FOR_TEAM;
                break;
            }
            case OPTION_PARTICIPANT: {
                action = PlayerService.ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID;
                break;
            }
        }
    }

    public AddPlayersFragment() {}

    /**
     * return new instance of this fragment with set arguments
     * @param option option to set
     * @param id to set
     * @return new instance of this fragment
     */
    public static AddPlayersFragment newInstance(int option, long id) {
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, id);
        fragment.setArguments(b);

        return fragment;
    }

    /**
     * return new instance of this fragment with set arguments
     * @param option option to set
     * @param id to set
     * @param omitPlayers players to omit from the list
     * @return new instance of this fragment
     */
    public static AddPlayersFragment newInstance(int option, long id, ArrayList<Player> omitPlayers){
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, id);
        b.putParcelableArrayList(ExtraConstants.EXTRA_OMIT, omitPlayers);
        fragment.setArguments(b);

        return fragment;
    }

    /**
     * return new instance of this fragment with set arguments
     * @param option option to set
     * @param id to set
     * @param omitPlayerIds ids of players to omit from the list
     * @param tmp actually useless integer argument to differentiate from other newInstance method
     * @return new instance of this fragment
     */
    public static AddPlayersFragment newInstance(int option, long id, ArrayList<PlayerStat> omitPlayerIds, int tmp){
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, id);
        b.putParcelableArrayList(ExtraConstants.EXTRA_OMIT_IDS, omitPlayerIds);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected AbstractSelectableListAdapter<Player, ? extends OneActionViewHolder> getAdapter() {
        return new SelectPlayersAdapter();
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (intent.getAction() == PlayerService.ACTION_GET_PLAYERS_FOR_TEAM) {
            Bundle bundle = getArguments();
            ArrayList<Player> omitPlayers = bundle.getParcelableArrayList(ExtraConstants.EXTRA_OMIT);
            if (omitPlayers != null) {
                ArrayList<Player> allPlayers = intent.getParcelableArrayListExtra(getDataKey());
                allPlayers.removeAll(omitPlayers);
                intent.putExtra(getDataKey(), allPlayers);
            }
        }
        if (intent.getAction() == PlayerService.ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID) {
            Bundle bundle = getArguments();
            ArrayList<PlayerStat> omitPlayers = bundle.getParcelableArrayList(ExtraConstants.EXTRA_OMIT_IDS);
            if (omitPlayers != null) {
                ArrayList<Player> allPlayers = intent.getParcelableArrayListExtra(getDataKey());
                ArrayList<Player> playersToShow = new ArrayList<>(allPlayers);
                for (Player p : allPlayers) {
                    for (PlayerStat omitP : omitPlayers)
                        if (p.getId() == omitP.getPlayerId())
                            playersToShow.remove(p);
                }
                intent.putExtra(getDataKey(), playersToShow);
            }
        }
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

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(action, getContext());
        Bundle bundle = getArguments();
        intent.putExtra(ExtraConstants.EXTRA_ID, bundle.getLong(ExtraConstants.EXTRA_ID));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        if (action == null) return true;
        return PlayerService.isWorking(action);
    }

    @Override
    protected void registerReceivers() {
        if (action == null) return;
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(action));
    }

    @Override
    protected void unregisterReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
