package fit.cvut.org.cz.squash.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;

/**
 * This DAO handles statistis
 * Created by Vaclav on 21. 4. 2016.
 */
public interface IStatDAO {

    /**
     * Insert stat to datasource
     * @param context
     * @param stat to be inserted
     */
    void insert(Context context, DStat stat);

    /**
     *
     * @param context
     * @param id of participant
     * @return List ids of players that are part of praticipant with specified id
     */
    ArrayList<Long> getPlayerIdsForParticipant(Context context, long id);

    /**
     * Delete all statistics with specified participantId and type
     * @param context
     * @param participantId
     * @param type type of statistic
     */
    void delete(Context context, long participantId, StatsEnum type);

    /**
     * Update stat in param to match its values in data source
     * @param context
     * @param stat
     */
    void update(Context context, DStat stat);

    /**
     * Delete all stats bound to target participant
     * @param context
     * @param participantId
     */
    void deleteByParticipant(Context context, long participantId);

    /**
     *
     * @param context
     * @param participantId
     * @param type
     * @return List of stats bound to specified participant and are of target type
     */
    ArrayList<DStat> getByParticipant(Context context, long participantId, StatsEnum type);

    /**
     *
     * @param context
     * @param tournamentId
     * @param type
     * @return List of stats of specified type bound to specified tournament
     */
    ArrayList<DStat> getByTournament(Context context, long tournamentId, StatsEnum type);

    /**
     *
     * @param context
     * @param competitionId
     * @param type
     * @return List of stats of specified type bound to specified competition
     */
    ArrayList<DStat> getByCompetition(Context context, long competitionId, StatsEnum type);

    /**
     *
     * @param context
     * @param type
     * @return List of stats of specified type
     */
    ArrayList<DStat> getAll(Context context, StatsEnum type);
    ArrayList<DStat> getByPlayer(Context context, long playerId, StatsEnum type);

    /**
     *
     * @param context
     * @param playerId
     * @param tournamentId
     * @param type
     * @return List of stats of specified type bound to specified tournament and specified player
     */
    ArrayList<DStat> getByPlayerAndTournament(Context context, long playerId, long tournamentId, StatsEnum type);

    /**
     *
     * @param context
     * @param playerId
     * @param competitionId
     * @param type
     * @return List of stats of specified type bound to specified competition and specified player
     */
    ArrayList<DStat> getByPlayerAndCompetition(Context context, long playerId, long competitionId, StatsEnum type);
}
