package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by atgot_000 on 1. 4. 2016.
 */
public class PlayerDetailFragment extends AbstractDataFragment {

    private TextView name, email, note;
    private static final String PLAYER_KEY = "extra_player";
    protected long playerID;

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
        args.putLong(PLAYER_KEY, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Log.d("PDF", "Get parcelable at: "+PLAYER_KEY);
        Player player = intent.getParcelableExtra(PLAYER_KEY);
        Log.d("PDF", "test");

        if( player == null ) {
            name.setText( "Player not found" ); //PROZATIMNI RESENI
            return;
        }
        Log.d("PDF", player.getEmail());

        name.setText(player.getName());
        email.setText(player.getEmail());
        note.setText(player.getNote());
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(PlayerService.EXTRA_ID, playerID);
        Log.d("PDF", "Asked for player: " + playerID);
        Log.d("PDF", "Action was: "+PlayerService.ACTION_GET_BY_ID);
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

        name = (TextView) v.findViewById(R.id.player_name);
        email  = (TextView) v.findViewById(R.id.player_email);
        note = (TextView) v.findViewById(R.id.player_note);

        if( getArguments() != null )
            playerID = getArguments().getLong(PLAYER_KEY);

        return v;
    }

}
