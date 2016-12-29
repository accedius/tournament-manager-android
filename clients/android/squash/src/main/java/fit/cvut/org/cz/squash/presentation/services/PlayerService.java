package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
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
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Players
 * Created by Vaclav on 3. 4. 2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {
    public PlayerService() {
        super("Squash player Service");
    }

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
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action){
            case ACTION_GET_PLAYERS_FOR_COMPETITION:{
                Intent result = new Intent(action);
                long competitionId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayersComplement(competitionId);
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));
                result.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_TOURNAMENT:{
                Intent result = new Intent(action);
                long tournamentId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayersComplement(tournamentId);
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));
                result.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_COMPETITION:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(id);
                for (Player player : players)
                    ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).addPlayer(competition, player);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_ADD_PLAYERS_TO_TOURNAMENT:{
                Intent result = new Intent(action);

                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                for (Player player : players)
                    ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).addPlayer(player.getId(), id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE_PLAYERS_IN_TEAM:{
                ArrayList<Player> players = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).updatePlayersInTeam(id, players);

                break;
            }
            case ACTION_GET_PLAYERS_FOR_TEAM:{
                Intent result = new Intent(action);
                Team t = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                t.getPlayers().addAll(((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getFreePlayers(t.getTournamentId()));
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(t.getPlayers()));
                result.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }

            case ACTION_GET_PLAYERS_IN_MATCH:{
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
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
                result.putExtra(ExtraConstants.EXTRA_HOME_PARTICIPANT, home);
                result.putExtra(ExtraConstants.EXTRA_AWAY_PARTICIPANT, away);
                result.putExtra(ExtraConstants.EXTRA_HOME_NAME, homeName);
                result.putExtra(ExtraConstants.EXTRA_AWAY_NAME, awayName);
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS, new ArrayList<>(homePlayers));
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS, new ArrayList<>(awayPlayers));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PLAYERS_FOR_MATCH:{
                Intent res = new Intent(action);
                Match match = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayers(match.getTournamentId());

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));
                res.putIntegerArrayListExtra(ExtraConstants.EXTRA_SELECTED, new ArrayList<Integer>());

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_COMPETITION:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(ExtraConstants.EXTRA_PLAYER_ID, -1);
                long competitionId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                result.putExtra(ExtraConstants.EXTRA_POSITION, position);
                result.putExtra(ExtraConstants.EXTRA_RESULT, ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).removePlayerFromCompetition(playerId, competitionId));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_PLAYER_FROM_TOURNAMENT:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                long playerId = intent.getLongExtra(ExtraConstants.EXTRA_PLAYER_ID, -1);
                long tournamentId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                result.putExtra(ExtraConstants.EXTRA_POSITION, position);
                result.putExtra(ExtraConstants.EXTRA_RESULT, ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).removePlayerFromTournament(playerId, tournamentId));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return intent;
    }
}
