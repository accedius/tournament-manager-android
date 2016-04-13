package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.SelectPlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Created by Vaclav on 12. 4. 2016.
 */
public class AddPlayersFragment extends AbstractSelectableListFragment<Player> {

    private static final String ARG_OPTION = "arg_option";
    private static final String ARG_ID = "arg_id";
    public static final int OPTION_COMPETITION = 0;
    public static final int OPTION_TOURNAMENT = 1;

    private String action = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (getArguments().getInt(ARG_OPTION, -1)){
            case 0:
                action = PlayerService.ACTION_GET_PLAYERS_FOR_COMPETITION;
                break;

            default:
                break;
        }

    }

    public AddPlayersFragment() {}
    public static AddPlayersFragment newInstance(int option, long id){
        AddPlayersFragment fragment = new AddPlayersFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_OPTION, option);
        b.putLong(ARG_ID, id);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected AbstractSelectableListAdapter<Player, ? extends OneActionViewHolder> getAdapter() {
        return new SelectPlayersAdapter();
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
    protected void askForData() {

        Long id = getArguments().getLong(ARG_ID, -1);
        Log.d("Add Players Fragment", String.format("action: %s, id: %d", action, id));
        if (action == null || id == -1) return;
        Intent intent = PlayerService.newStartIntent(action, getContext());
        intent.putExtra(PlayerService.EXTRA_ID, id);

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

        try{
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
