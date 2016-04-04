package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.PlayerAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class PlayersListFragment extends AbstractListFragment {

    private String action = "org.cz.cvut.tournamentmanager.action.players";

    //private DataReceiver receiver = new DataReceiver();

    /*public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return this.action;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void askForData() {
        Intent intent = PlayerService.getStartIntent(this.action, getActivity());
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(action);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(action));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new PlayerAdapter();
    }

    @Override
    protected String getDataKey() {
        return PlayerService.EXTRA_RESULT;
    }


}
