package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Players
 * Created by Vaclav on 3. 4. 2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {
    public PlayerService() {
        super("Squash player Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_PLAYERS = "extra_players";
    public static final String EXTRA_SELECTED = "extra_selected";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_ROLE = "extra_role";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_PLAYER_ID = "extra_player_id";
    public static final String EXTRA_HOME_NAME = "extra_home_name";
    public static final String EXTRA_AWAY_NAME = "extra_away_name";
    public static final String EXTRA_HOME_PARTICIPANT = "extra_home_participant";
    public static final String EXTRA_AWAY_PARTICIPANT = "extra_away_participant";
    public static final String EXTRA_HOME_PLAYERS = "extra_home_players";
    public static final String EXTRA_AWAY_PLAYERS = "extra_away_players";

    public static final String ACTION_GET_PLAYERS_FOR_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.get_players_for_competition";
    public static final String ACTION_ADD_PLAYERS_TO_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.add_players_to_competition";
    public static final String ACTION_DELETE_PLAYER_FROM_COMPETITION = "fit.cvut.org.cz.squash.presentation.services.delete_player_from_competition";

    public static final String ACTION_GET_PLAYERS_FOR_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_players_for_tournament";
    public static final String ACTION_ADD_PLAYERS_TO_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.add_players_to_tournament";
    public static final String ACTION_DELETE_PLAYER_FROM_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.delete_player_from_tournament";

    public static final String ACTION_UPDATE_PLAYERS_IN_TEAM = "fit.cvut.org.cz.squash.presentation.services.update_players_in_team";
    public static final String ACTION_GET_PLAYERS_FOR_TEAM = "fit.cvut.org.cz.squash.presentation.services.get_players_for_team";

    public static final String ACTION_GET_PLAYERS_IN_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_players_in_match";
    public static final String ACTION_GET_PLAYERS_FOR_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_home_players_for_match";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_GET_PLAYERS_FOR_COMPETITION:{
                Intent result = new Intent(action);
                long competitionId = intent.getLongExtra(EXTRA_ID, -1);
                List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayersComplement(competitionId);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, new ArrayList<>(players));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_TOURNAMENT:{
                Intent result = new Intent(action);
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayersComplement(tournamentId);
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, new ArrayList<>(players));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_COMPETITION:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(id);
                for (Player player : players)
                    ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).addPlayer(competition, player);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_TOURNAMENT:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                for (Player player : players)
                    ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).addPlayer(player.getId(), id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE_PLAYERS_IN_TEAM:{
                ArrayList<Player> players = intent.getParcelableArrayListExtra(EXTRA_PLAYERS);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).updatePlayersInTeam(id, players);

                break;
            }
            case ACTION_GET_PLAYERS_FOR_TEAM:{
                Intent result = new Intent(action);
                Team t = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(intent.getLongExtra(EXTRA_ID, -1));
                t.getPlayers().addAll(((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getFreePlayers(t.getTournamentId()));
                result.putParcelableArrayListExtra(EXTRA_PLAYERS, new ArrayList<>(t.getPlayers()));
                result.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }

            case ACTION_GET_PLAYERS_IN_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);

                Match match = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(id);
                Tournament tournament = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).getById(match.getTournamentId());
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(tournament.getCompetitionId());

                Participant home = null, away = null;
                for (Participant participant : match.getParticipants()) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        home = participant;
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        away = participant;
                }

                List<PlayerStat> homePlayers = ((IPlayerStatManager)ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class)).getByParticipantId(home.getId());
                List<PlayerStat> awayPlayers = ((IPlayerStatManager)ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class)).getByParticipantId(away.getId());

                String homeName = null, awayName = null;
                if (CompetitionTypes.individuals().equals(competition.getType())) {
                    Player homePlayer = ManagerFactory.getInstance(this).getEntityManager(Player.class).getById(home.getParticipantId());
                    Player awayPlayer = ManagerFactory.getInstance(this).getEntityManager(Player.class).getById(away.getParticipantId());
                    homeName = homePlayer.getName();
                    awayName = awayPlayer.getName();
                } else {
                    Team homeTeam = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(home.getParticipantId());
                    Team awayTeam = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(away.getParticipantId());
                    homeName = homeTeam.getName();
                    awayName = awayTeam.getName();
                }
                result.putExtra(EXTRA_HOME_PARTICIPANT, home);
                result.putExtra(EXTRA_AWAY_PARTICIPANT, away);
                result.putExtra(EXTRA_HOME_NAME, homeName);
                result.putExtra(EXTRA_AWAY_NAME, awayName);
                result.putParcelableArrayListExtra(EXTRA_HOME_PLAYERS, new ArrayList<>(homePlayers));
                result.putParcelableArrayListExtra(EXTRA_AWAY_PLAYERS, new ArrayList<>(awayPlayers));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_MATCH:{
                Intent res = new Intent(action);
                Match match = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(intent.getLongExtra(EXTRA_ID, -1));
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayers(match.getTournamentId());

                res.putParcelableArrayListExtra(EXTRA_PLAYERS, new ArrayList<>(players));
                res.putIntegerArrayListExtra(EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_COMPETITION:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(EXTRA_PLAYER_ID, -1);
                long competitionId = intent.getLongExtra(EXTRA_ID, -1);
                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).removePlayerFromCompetition(playerId, competitionId));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_TOURNAMENT:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(EXTRA_PLAYER_ID, -1);
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).removePlayerFromTournament(playerId, tournamentId));

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
