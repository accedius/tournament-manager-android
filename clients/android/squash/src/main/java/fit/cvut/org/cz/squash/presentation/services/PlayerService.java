package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {

    public PlayerService() {
        super("Squash Player Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PLAYERS = "extra_players";
    public static final String EXTRA_SELECTED = "extra_selected";

    public static final String ACTION_GET_SELECTED_FROM_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.competition_selected_players";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0, "Stan", null, null));
        players.add(new Player(0, "Kyle", null, null));
        players.add(new Player(0, "Cartman", null, null));
        players.add(new Player(0, "Kenny", null, null));
        players.add(new Player(0, "Butters", null, null));

        ArrayList<Integer> sel = new ArrayList<>();
        sel.add(0);
        sel.add(2);
        sel.add(4);

        Intent result = new Intent(action);
        result.putParcelableArrayListExtra(EXTRA_PLAYERS, players);
        result.putIntegerArrayListExtra(EXTRA_SELECTED, sel);

        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
