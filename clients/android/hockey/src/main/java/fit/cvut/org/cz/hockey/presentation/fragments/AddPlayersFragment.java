package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.SelectPlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Fragment for adding players
 * Created by atgot_000 on 15. 4. 2016.
 */
public class AddPlayersFragment extends AbstractSelectableListFragment<Player> {
    private static final String ARG_OPTION = "arg_option";
    private static final String ARG_ID = "arg_id";
    private static final String ARG_OMIT = "arg_omit";
    private static final String ARG_OMIT_ID = "arg_omit_ids";
    public static final int OPTION_COMPETITION = 1;
    public static final int OPTION_TOURNAMENT = 2;
    public static final int OPTION_TEAM = 3;
    public static final int OPTION_PARTICIPANT = 4;

    private String action = null;

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
        b.putInt(ARG_OPTION, option);
        b.putLong(ARG_ID, id);
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
        b.putInt(ARG_OPTION, option);
        b.putLong(ARG_ID, id);
        b.putParcelableArrayList(ARG_OMIT, omitPlayers);
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
        b.putInt(ARG_OPTION, option);
        b.putLong(ARG_ID, id);
        b.putParcelableArrayList(ARG_OMIT_ID, omitPlayerIds);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int option = getArguments().getInt(ARG_OPTION, -1);

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

    @Override
    protected AbstractSelectableListAdapter<Player, ? extends OneActionViewHolder> getAdapter() {
        return new SelectPlayersAdapter();
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (intent.getAction() == PlayerService.ACTION_GET_PLAYERS_FOR_TEAM) {
            ArrayList<Player> omitPlayers = getArguments().getParcelableArrayList(ARG_OMIT);
            if (omitPlayers != null) {
                ArrayList<Player> allPlayers = intent.getParcelableArrayListExtra(getDataKey());
                allPlayers.removeAll(omitPlayers);
                intent.putExtra(getDataKey(), allPlayers);
            }
        }
        if (intent.getAction() == PlayerService.ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID) {
            ArrayList<PlayerStat> omitPlayers = getArguments().getParcelableArrayList(ARG_OMIT_ID);
            if (omitPlayers != null) {
                ArrayList<Player> allPlayers = intent.getParcelableArrayListExtra(getDataKey());
                ArrayList<Player> playersToShow = new ArrayList<>(allPlayers);
                for (Player p : allPlayers) {
                    for (PlayerStat omitP : omitPlayers)
                        if (p.getId() == omitP.getPlayerId()) playersToShow.remove(p);
                }
                intent.putExtra(getDataKey(), playersToShow);
            }
        }
        super.bindDataOnView(intent);
    }

    @Override
    protected String getDataKey() {
        return PlayerService.EXTRA_PLAYERS;
    }

    @Override
    protected String getDataSelectedKey() {
        return PlayerService.EXTRA_SELECTED;
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(action, getContext());
        intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));

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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(action));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
