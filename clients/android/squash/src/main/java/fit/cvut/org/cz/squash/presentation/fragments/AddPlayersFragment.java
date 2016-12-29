package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.SelectPlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/** Fragment that allows user to select players and then returns them
 * Created by Vaclav on 12. 4. 2016.
 */
public class AddPlayersFragment extends AbstractSelectableListFragment<Player> {
    public static final int OPTION_COMPETITION = 0;
    public static final int OPTION_TOURNAMENT = 1;
    public static final int OPTION_TEAM = 2;
    public static final int OPTION_MATCH = 3;

    private String action = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (getArguments().getInt(ExtraConstants.EXTRA_OPTION, -1)){
            case 0:
                action = PlayerService.ACTION_GET_PLAYERS_FOR_COMPETITION;
                break;
            case 1:
                action = PlayerService.ACTION_GET_PLAYERS_FOR_TOURNAMENT;
                break;
            case 2:
                action = PlayerService.ACTION_GET_PLAYERS_FOR_TEAM;
                break;
            case 3:
                action = PlayerService.ACTION_GET_PLAYERS_FOR_MATCH;
                break;
            default:
                break;
        }
    }

    public AddPlayersFragment() {}
    public static AddPlayersFragment newInstance(int option, long id){
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, id);
        fragment.setArguments(b);

        return fragment;
    }
    public static AddPlayersFragment newInstance(int option, long id, ArrayList<Player> omitPlayers){
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, id);
        b.putParcelableArrayList(ExtraConstants.EXTRA_OMIT, omitPlayers);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ArrayList<Player> omitPlayers = getArguments().getParcelableArrayList(ExtraConstants.EXTRA_OMIT);
        if (omitPlayers != null) {
            ArrayList<Player> players = intent.getParcelableArrayListExtra(getDataKey());
            players.removeAll(omitPlayers);
            intent.putExtra(getDataKey(), players);
        }
        super.bindDataOnView(intent);
    }

    @Override
    protected AbstractSelectableListAdapter<Player, ? extends OneActionViewHolder> getAdapter() {
        return new SelectPlayersAdapter();
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
        Long id = getArguments().getLong(ExtraConstants.EXTRA_ID, -1);
        if (action == null || id == -1) return;
        Intent intent = PlayerService.newStartIntent(action, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, id);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return action == null || PlayerService.isWorking(action);
    }

    @Override
    protected void registerReceivers() {
        if (action == null) return;
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(action));
    }

    @Override
    protected void unregisterReceivers() {
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
