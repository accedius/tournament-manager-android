package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
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
    public static final String EXTRA_ID = "extra_id";

    public static final String ACTION_GET_SELECTED_FROM_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.competition_selected_players";
    public static final String ACTION_GET_PLAYERS_FOR_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.get_players_for_competition";
    public static final String ACTION_ADD_PLAYERS_TO_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.add_players_to_competition";

    public static final String ACTION_ADD_PLAYERS_TO_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.add_players_to_tournament";
    public static final String ACTION_GET_PLAYERS_FOR_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_players_for_tournament";


    public static final String ACTION_UPDATE_PLAYERS_IN_TEAM = "fit.cvut.org.cz.squash.presentation.services.update_players_in_team";
    public static final String ACTION_GET_PLAYERS_FOR_TEAM = "fit.cvut.org.cz.squash.presentation.services.get_players_for_team";



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

        switch (action){
            case ACTION_GET_PLAYERS_FOR_COMPETITION:{
                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, ManagersFactory.getInstance().playerManager.getPlayersNotInCompetition(this, intent.getLongExtra(EXTRA_ID, -1)));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_TOURNAMENT:{
                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, ManagersFactory.getInstance().playerManager.getPlayersNotInTournament(this, intent.getLongExtra(EXTRA_ID, -1)));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_COMPETITION:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                for (Player p: players) ManagersFactory.getInstance().playerManager.addPlayerToCompetition(this, p.getId(), id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_TOURNAMENT:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                for (Player p: players) ManagersFactory.getInstance().playerManager.addPlayerToTournament(this, p.getId(), id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE_PLAYERS_IN_TEAM:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagersFactory.getInstance().playerManager.updatePlayersInTeam(this, id, players);

                //LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_TEAM:{
                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, ManagersFactory.getInstance().playerManager.getPlayersNotInTeams(this, intent.getLongExtra(EXTRA_ID, -1)));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
