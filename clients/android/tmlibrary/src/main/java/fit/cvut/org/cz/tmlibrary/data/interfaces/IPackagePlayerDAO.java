package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public interface IPackagePlayerDAO {
    /**
     *
     * @param context application context
     * @param playerId id of the player to be inserted
     * @param competitionId id of the competition the player should be inserted to
     */
    void addPlayerToCompetition(Context context, long playerId, long competitionId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be inserted
     * @param tournamentId id of the tournament the player should be inserted to
     */
    void addPlayerToTournament(Context context, long playerId, long tournamentId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be inserted
     * @param matchId id of the match the player should be inserted to
     */
    void addPlayerToMatch(Context context, long playerId, long matchId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be inserted
     * @param teamId id of the team the player should be inserted to
     */
    void addPlayerToTeam(Context context, long playerId, long teamId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be deleted
     * @param competitionId id of the competition the player should be deleted from
     */
    void deletePlayerFromCompetition(Context context, long playerId, long competitionId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be deleted
     * @param tournamentId id of the tournament the player should be deleted from
     */
    void deletePlayerFromTournament(Context context, long playerId, long tournamentId);

    /**
     *
     * @param context application context
     * @param playerId id of the player to be deleted
     * @param matchId id of the match the player should be deleted from
     */
    void deletePlayerFromMatch(Context context, long playerId, long matchId);

    /**
     * delete all players from chosen team
     * @param context application context
     * @param teamId id of the team to be wiped
     */
    void deleteAllPlayersFromTeam(Context context, long teamId);

    /**
     *
     * @param context application context
     * @param competitionId id of the competition where players should be found
     * @return list of found player ids
     */
    ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId);

    /**
     *
     * @param context application context
     * @param tournamentId id of the tournament where players should be found
     * @return list of found player ids
     */
    ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId);

    /**
     *
     * @param context application context
     * @param matchId id of the match where players should be found
     * @return list of found player ids
     */
    ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId);

    /**
     *
     * @param context application context
     * @param teamId id of the team where players should be found
     * @return list of found player ids
     */
    ArrayList<Long> getPlayerIdsByTeam(Context context, long teamId);

    /**
     *
     * @param context application context
     * @param participantId id of the participant where players should be found
     * @return list of found player ids
     */
    ArrayList<Long> getPlayerIdsByParticipant(Context context, long participantId);

    /**
     * get all players from core
     * @param context application context
     * @return all players in core
     */
    Map<Long, Player> getAllPlayers(Context context);

    /**
     * insert player to DB
     * @param context application context
     * @param values player values - name, email, note
     * @return playerID of inserted player
     */
    long insertPlayer(Context context, ContentValues values);

    /**
     * insert player to DB
     * @param context application context
     * @param values player values - name, email, note
     */
    void updatePlayer(Context context, ContentValues values);

}
