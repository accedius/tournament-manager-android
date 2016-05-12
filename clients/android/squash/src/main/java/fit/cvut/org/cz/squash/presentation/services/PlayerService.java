package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
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
    public static final String EXTRA_ROLE = "extra_role";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_PLAYER_ID = "extra_player_id";
    public static final String EXTRA_HOME_PLAYERS = "extra_home_players";
    public static final String EXTRA_AWAY_PLAYERS = "extra_away_players";

    public static final String ACTION_GET_SELECTED_FROM_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.competition_selected_players";
    public static final String ACTION_GET_PLAYERS_FOR_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.get_players_for_competition";
    public static final String ACTION_ADD_PLAYERS_TO_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.add_players_to_competition";
    public static final String ACTION_DELETE_PLAYER_FROM_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.delete_player_from_competition";

    public static final String ACTION_ADD_PLAYERS_TO_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.add_players_to_tournament";
    public static final String ACTION_GET_PLAYERS_FOR_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_players_for_tournament";
    public static final String ACTION_DELETE_PLAYER_FROM_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.delete_player_from_tournament";


    public static final String ACTION_UPDATE_PLAYERS_IN_TEAM = "fit.cvut.org.cz.squash.presentation.services.update_players_in_team";
    public static final String ACTION_GET_PLAYERS_FOR_TEAM = "fit.cvut.org.cz.squash.presentation.services.get_players_for_team";

    public static final String ACTION_GET_AWAY_PLAYERS_IN_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_away_players_in_match";
    public static final String ACTION_GET_HOME_PLAYERS_IN_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_home_players_in_match";
    public static final String ACTION_GET_PLAYERS_IN_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_players_in_match";
    public static final String ACTION_GET_PLAYERS_FOR_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_home_players_for_match";
    public static final String ACTION_UDATE_PLAYERS_FOR_MATCH = "fit.cvut.org.cz.squash.presentation.services.update_players_for_match";



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

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagersFactory.getInstance().playerManager.updatePlayersInTeam(this, id, players);

                break;
            }
            case ACTION_GET_PLAYERS_FOR_TEAM:{
                Intent result = new Intent(action);
                Team t = ManagersFactory.getInstance().teamsManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                t.getPlayers().addAll(ManagersFactory.getInstance().playerManager.getPlayersNotInTeams(this, t.getTournamentId()));
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, t.getPlayers());
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_AWAY_PLAYERS_IN_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, ManagersFactory.getInstance().statsManager.getPlayersForMatch(this, id, "away"));
                long teamID = ManagersFactory.getInstance().participantManager.getTeamIdForMatchParticipant(this, id, "away");
                result.putExtra(EXTRA_ID, teamID);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_HOME_PLAYERS_IN_MATCH: {
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, ManagersFactory.getInstance().statsManager.getPlayersForMatch(this, id, "home"));
                long teamID = ManagersFactory.getInstance().participantManager.getTeamIdForMatchParticipant(this, id, "home");
                result.putExtra(EXTRA_ID, teamID);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }

            case ACTION_GET_PLAYERS_IN_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);

                long homeTeamId = ManagersFactory.getInstance().participantManager.getTeamIdForMatchParticipant(this, id, "home");
                long awayTeamId = ManagersFactory.getInstance().participantManager.getTeamIdForMatchParticipant(this, id, "away");

                Team homeTeam = ManagersFactory.getInstance().teamsManager.getById(this, homeTeamId);
                homeTeam.setPlayers(ManagersFactory.getInstance().statsManager.getPlayersForMatch(this, id, "home"));

                Team awayTeam = ManagersFactory.getInstance().teamsManager.getById(this, awayTeamId);
                awayTeam.setPlayers(ManagersFactory.getInstance().statsManager.getPlayersForMatch(this, id, "away"));

                result.putExtra(EXTRA_HOME_PLAYERS, homeTeam);
                result.putExtra(EXTRA_AWAY_PLAYERS, awayTeam);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_MATCH:{
                Intent result = new Intent(action);
                Team t = ManagersFactory.getInstance().teamsManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, t.getPlayers());
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UDATE_PLAYERS_FOR_MATCH:{
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                String role = intent.getStringExtra(EXTRA_ROLE);
                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                if (players != null){
                    ManagersFactory.getInstance().participantManager.updatePlayersForMatch(this, matchId, role, players);
                } else ManagersFactory.getInstance().participantManager.setParticipationValid(this, matchId);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_COMPETITION:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(EXTRA_PLAYER_ID, -1);
                long competitionId = intent.getLongExtra(EXTRA_ID, -1);
                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, ManagersFactory.getInstance().playerManager.deletePlayerFromCompetition(this, playerId, competitionId));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_TOURNAMENT:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(EXTRA_PLAYER_ID, -1);
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, ManagersFactory.getInstance().playerManager.deletePlayerFromTournament(this, playerId, tournamentId));

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
