package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public interface IPackagePlayerManager {
    /**
     *
     * @param context application context
     * @param playerId id of the player to be added
     * @param competitionId id of the competition where player should be added
     */
    void addPlayerToCompetition(Context context, long playerId, long competitionId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be added
     * @param tournamentId id of the tournament where player should be added
     */
    void addPlayerToTournament(Context context, long playerId, long tournamentId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be removed
     * @param competitionId id of the competition the player should be removed from
     * @return true if ok, false if player is registered somewhere and thus cannot be deleted
     */
    boolean deletePlayerFromCompetition(Context context, long playerId, long competitionId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be removed
     * @param tournamentId id of the tournament the player should be removed from
     * @return true if ok, false if player is registered somewhere and thus cannot be deleted
     */
    boolean deletePlayerFromTournament(Context context, long playerId, long tournamentId);

    /**
     *
     * @param context application context
     * @param competitionId id of the competition
     * @return players in competition
     */
    ArrayList<Player> getPlayersByCompetition(Context context, long competitionId);

    /**
     *
     * @param context application context
     * @param tournamentId id of the tournament
     * @return players in tournament
     */
    ArrayList<Player> getPlayersByTournament(Context context, long tournamentId);

    /**
     *
     * @param context application context
     * @param participantId id of the participant
     * @return players in participant
     */
    ArrayList<Player> getPlayersByParticipant(Context context, long participantId);

    /**
     *
     * @param context application context
     * @param teamId id of the team
     * @return players in team
     */
    ArrayList<Player> getPlayersByTeam(Context context, long teamId);

    /**
     * get player by his id
     * @param context application context
     * @param playerId id of the player to be found
     * @return found player
     */
    Player getPlayerById(Context context, long playerId);

    /**
     * get all player from core
     * @param context application context
     * @return arraylist of all players
     */
    ArrayList<Player> getAllPlayers(Context context);

    /**
     * get all player from core
     * @param context application context
     * @param player inserted player
     * @return player id
     */
    long insertPlayer(Context context, Player player);

    /**
     * get all player from core
     * @param context application context
     * @param player inserted player
     */
    void updatePlayer(Context context, Player player);

    /**
     * get players that are not registered in certain competition
     * @param context application context
     * @param competitionId id of the competition
     * @return found players
     */
    ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId);

    /**
     *
     * @param context application context
     * @param tournamentId id of the tournament
     * @return players in tournament
     */
    ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId);

    /**
     * set team roster to defined players
     * @param context application context
     * @param teamId id of the team
     * @param players players to be set the roster to
     */
    void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players);

    /**
     * get players that are in tournament, bau are not in any team
     * @param context application context
     * @param tournamentId id of the tournament
     * @return found players
     */
    ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId);

    /**
     * update player that are in certain participant
     * @param context application context
     * @param participantId id of the participant
     * @param competitionId id of the competition
     * @param tournamentId id of the tournament
     * @param players players to be set the roster to
     */
    void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players);

    /**
     * get players that are not in participant
     * @param context application context
     * @param participantId id of the participant
     * @return found players
     */
    ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId);
}
