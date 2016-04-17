package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 15. 4. 2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_PLAYERS = "extra_players";
    public static final String EXTRA_SELECTED = "extra_selected";

    public static final String ACTION_GET_PLAYERS_NOT_IN_COMPETITION = "action_get_players_not_in_competition";
    public static final String ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT = "action_get_players_not_in_tournament";

    public static final String ACTION_ADD_PLAYERS_TO_COMPETITION = "action_add_players_to_competition";
    public static final String ACTION_ADD_PLAYERS_TO_TOURNAMENT = "action_add_players_to_tournament";

    public static final String ACTION_GET_PLAYERS_NOT_IN_TEAMS = "action_get_players_not_in_teams";
    public static final String ACTION_UPDATE_TEAM_PLAYERS = "action_update_team_players";

    public PlayerService() {
        super("Hockey Player Service");
    }

    public static Intent newStartIntent(String action, Context context)
    {
        Intent res = new Intent(context, PlayerService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra( EXTRA_ACTION );

        switch (action)
        {
            case ACTION_GET_PLAYERS_NOT_IN_COMPETITION:
            {
                Long id = intent.getLongExtra( EXTRA_ID, -1 );
                ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersNotInCompetition(this, id);
                Intent res = new Intent();
                res.setAction( ACTION_GET_PLAYERS_NOT_IN_COMPETITION );
                res.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());
                res.putParcelableArrayListExtra( EXTRA_PLAYERS, players );

                LocalBroadcastManager.getInstance( this ).sendBroadcast( res );

                break;
            }
            case ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT:
            {
                Long id = intent.getLongExtra( EXTRA_ID, -1 );
                ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersNotInTournament(this, id);
                Intent res = new Intent();
                res.setAction( ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT );
                res.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());
                res.putParcelableArrayListExtra( EXTRA_PLAYERS, players );

                LocalBroadcastManager.getInstance( this ).sendBroadcast( res );

                break;
            }
            case ACTION_ADD_PLAYERS_TO_COMPETITION:
            {
                Intent result = new Intent(action);
                ArrayList<Player> players = intent.getParcelableArrayListExtra( EXTRA_PLAYERS );
                long id = intent.getLongExtra(EXTRA_ID, -1);

                for( Player p : players ) {
                    ManagerFactory.getInstance().packagePlayerManager.addPlayerToCompetition(this, p.getId(), id);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_TOURNAMENT:
            {
                Intent result = new Intent(action);
                ArrayList<Player> players = intent.getParcelableArrayListExtra( EXTRA_PLAYERS );
                long id = intent.getLongExtra(EXTRA_ID, -1);

                for( Player p : players ) {
                    ManagerFactory.getInstance().packagePlayerManager.addPlayerToTournament(this, p.getId(), id);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);

                break;
            }
            case ACTION_GET_PLAYERS_NOT_IN_TEAMS:
            {
                Intent result = new Intent(action);
                ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersNotInTeams( this, intent.getLongExtra(EXTRA_ID, -1));

                result.putParcelableArrayListExtra( EXTRA_PLAYERS, players );
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance( this ).sendBroadcast( result );

                break;
            }
            case ACTION_UPDATE_TEAM_PLAYERS:
            {
                long id = intent.getLongExtra( EXTRA_ID, -1 );
                ArrayList<Player> players = intent.getParcelableArrayListExtra( EXTRA_PLAYERS );
                ManagerFactory.getInstance().packagePlayerManager.updatePlayersInTeam( this, id, players );

                break;
            }
        }

    }
}
