package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Fragment for create or edit Player.
 */
public class NewPlayerFragment extends fit.cvut.org.cz.tmlibrary.presentation.fragments.NewPlayerFragment {
    @Override
    protected String getPlayerKey() {
        return ExtraConstants.EXTRA_PLAYER;
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, playerId);
        PlayerService.enqueueWork(getContext(),intent, PlayerService.class);
        //getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(PlayerService.ACTION_GET_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(PlayerService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public Player getPlayer() {
        return new Player(getArguments().getLong(ExtraConstants.EXTRA_ID), name.getText().toString(), email.getText().toString(), note.getText().toString());
    }

}
