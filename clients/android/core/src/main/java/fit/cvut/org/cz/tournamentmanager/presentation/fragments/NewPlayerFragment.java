package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by kevin on 7. 4. 2016.
 */
public class NewPlayerFragment extends fit.cvut.org.cz.tmlibrary.presentation.fragments.NewPlayerFragment {

    @Override
    protected void savePlayer(Player c) {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_CREATE, getContext());
        intent.putExtra(PlayerService.EXTRA_PLAYER, c);

        getContext().startService(intent);
    }

    @Override
    protected void updatePlayer(Player c) {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_UPDATE, getContext());
        intent.putExtra(PlayerService.EXTRA_PLAYER, c);

        getContext().startService(intent);
    }

    @Override
    protected String getPlayerKey() {
        return PlayerService.EXTRA_PLAYER;
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(PlayerService.EXTRA_ID, PlayerId);

        getContext().startService(intent);
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
}
