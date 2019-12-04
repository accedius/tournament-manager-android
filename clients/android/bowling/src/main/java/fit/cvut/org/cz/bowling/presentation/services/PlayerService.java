package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Bowling player service to handle intent/service/activity work in player's scope
 */
public class PlayerService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_PLAYERS_NOT_IN_COMPETITION = "action_get_players_not_in_competition";
    public static final String ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT = "action_get_players_not_in_tournament";

    public static final String ACTION_DELETE_PLAYER_FROM_TOURNAMENT = "action_delete_player_from_tournament";
    public static final String ACTION_DELETE_PLAYER_FROM_COMPETITION = "action_delete_player_from_competition";

    public static final String ACTION_ADD_PLAYERS_TO_COMPETITION = "action_add_players_to_competition";
    public static final String ACTION_ADD_PLAYERS_TO_TOURNAMENT = "action_add_players_to_tournament";

    public static final String ACTION_GET_PLAYERS_FOR_TEAM = "action_get_players_for_team";
    public static final String ACTION_UPDATE_TEAM_PLAYERS = "action_update_team_players";

    public static final String ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID = "action_get_players_in_tournament_by_match_id";

    public PlayerService() {
        super("Bowling Player Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, PlayerService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action)
        {
            case ACTION_GET_PLAYERS_NOT_IN_COMPETITION:
            {
                Long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ICompetitionManager iCompetitionManager = iManagerFactory.getEntityManager(Competition.class);
                List<Player> players = iCompetitionManager.getCompetitionPlayersComplement(id);Intent res = new Intent();
                res.setAction(ACTION_GET_PLAYERS_NOT_IN_COMPETITION);
                res.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT:
            {
                Long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ITournamentManager iTournamentManager = iManagerFactory.getEntityManager(Tournament.class);
                List<Player> players = iTournamentManager.getTournamentPlayersComplement(id);
                Intent res = new Intent();
                res.setAction(ACTION_GET_PLAYERS_NOT_IN_TOURNAMENT);
                res.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_ADD_PLAYERS_TO_COMPETITION: {
                Intent result = new Intent(action);
                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ICompetitionManager iCompetitionManager = iManagerFactory.getEntityManager(Competition.class);
                Competition competition = iCompetitionManager.getById(id);

                for (Player player : players) {
                    iManagerFactory = ManagerFactory.getInstance(this);
                    iCompetitionManager = iManagerFactory.getEntityManager(Competition.class);
                    iCompetitionManager.addPlayer(competition, player);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_TOURNAMENT:
            {
                Intent result = new Intent(action);
                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                for (Player p : players) {
                    ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).addPlayer(p.getId(), id);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);

                break;
            }
            case ACTION_GET_PLAYERS_FOR_TEAM: {
                Intent result = new Intent(action);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ITeamManager iTeamManager = iManagerFactory.getEntityManager(Team.class);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Team t = iTeamManager.getById(id);
                iManagerFactory = ManagerFactory.getInstance(this);
                iTeamManager = iManagerFactory.getEntityManager(Team.class);
                List<Player> playerList = iTeamManager.getFreePlayers(t.getTournamentId());
                t.getPlayers().addAll(playerList);
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(t.getPlayers()));
                result.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);

                break;
            }
            case ACTION_UPDATE_TEAM_PLAYERS:
            {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ITeamManager iTeamManager = iManagerFactory.getEntityManager(Team.class);
                iTeamManager.updatePlayersInTeam(id, players);

                break;
            }
            case ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID: {
                Intent res = new Intent(action);
                Match match = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ITournamentManager iTournamentManager = iManagerFactory.getEntityManager(Tournament.class);
                List<Player> players = iTournamentManager.getTournamentPlayers(match.getTournamentId());

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));
                res.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_COMPETITION:
            {
                Intent res = new Intent(action);
                long playerId = intent.getLongExtra(ExtraConstants.EXTRA_PLAYER_ID, -1);
                long competitionId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                ICompetitionManager iCompetitionManager = iManagerFactory.getEntityManager(Competition.class);
                boolean result = iCompetitionManager.removePlayerFromCompetition(playerId, competitionId);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_TOURNAMENT:
            {
                Intent res = new Intent(action);
                long playerId = intent.getLongExtra(ExtraConstants.EXTRA_PLAYER_ID, -1);
                long tournamentId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                boolean result = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).removePlayerFromTournament(playerId, tournamentId);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}

