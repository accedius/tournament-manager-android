package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

import static fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants.EXTRA_PLAYER;

/**
 * Fragment for display Player detail.
 */
public class PlayerDetailFragment extends AbstractDataFragment {
    private TextView email, note;
    /**
     * Id of Player.
     */
    protected long playerId;

    /**
     * PlayerDetailFragment creator.
     * @param id id of Player
     * @param clazz class of fragment
     * @return PlayerDetailFragment instance
     */
    public static PlayerDetailFragment newInstance(long id, Class<? extends PlayerDetailFragment> clazz){
        PlayerDetailFragment fragment = null;
        try {
            Constructor<? extends PlayerDetailFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong(EXTRA_PLAYER, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Player player = intent.getParcelableExtra(EXTRA_PLAYER);
        getActivity().setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.player)+" – "+player.getName());
        email.setText(player.getEmail());
        note.setText(player.getNote());
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, playerId);
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

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_player_detail, container, false);

        email  = (TextView) v.findViewById(R.id.player_email);
        note = (TextView) v.findViewById(R.id.player_note);

        if (getArguments() != null)
            playerId = getArguments().getLong(EXTRA_PLAYER);

        return v;
    }

}
