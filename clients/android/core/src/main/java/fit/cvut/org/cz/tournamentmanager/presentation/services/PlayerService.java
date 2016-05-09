package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;
import fit.cvut.org.cz.tournamentmanager.business.ManagersFactory;

/**
 * Created by kevin on 4.4.2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_PLAYER = "extra_player";
    public static final String EXTRA_PLAYERS = "extra_players";
    public static final String EXTRA_PACKAGES = "extra_packages";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.tournamentmanager.presentation.services.new_player";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.tournamentmanager.presentation.services.get_player_by_id";
    public static final String ACTION_GET_ALL = "fit.cvut.org.cz.tournamentmanager.presentation.services.get_all_players";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.tournamentmanager.presentation.services.update_player";

    public PlayerService() {
        super("Player Service");
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(EXTRA_ACTION, action);
        Log.d("PlayerSRVC", "action:"+action);
        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        Log.d("PlayerSRVC", "do_work:"+action);

        switch (action){
            case ACTION_CREATE:{
                Player c = intent.getParcelableExtra(EXTRA_PLAYER);
                ManagersFactory.getInstance().playerManager.insert(this, c);
                break;
            }
            case ACTION_GET_ALL:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_ALL);
                ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getAll(this);
                result.putExtra(EXTRA_PLAYERS, players);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_ID:{
                Log.d("PlayerService", "Received, id: " + intent.getLongExtra(EXTRA_ID, -1));
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Player p = ManagersFactory.getInstance().playerManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                Log.d("PlayerService", "Answering with player "+p.getName());
                result.putExtra(EXTRA_PLAYER, p);
                Log.d("PlayerService", "Player set to: "+EXTRA_PLAYER);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE:{
                Player p = intent.getParcelableExtra(EXTRA_PLAYER);
                ManagersFactory.getInstance().playerManager.update(this, p);
                break;
            }
        }
    }
}
