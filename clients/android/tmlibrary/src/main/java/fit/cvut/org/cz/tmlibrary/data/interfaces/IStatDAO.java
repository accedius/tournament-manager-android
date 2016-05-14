package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public interface IStatDAO {

    /**
     * insert stat into database
     * @param context application context
     * @param stat stat to be inserted
     * @return id of inserted stat
     */
    long insert( Context context, DStat stat);

    /**
     * update existing stat in database
     * @param context application context
     * @param stat stat to be updated
     */
    void update( Context context, DStat stat);

    /**
     * delete stat from database
     * @param context application context
     * @param id id of the stat to be deleted
     */
    void delete( Context context, long id);

    /**
     *
     * @param context application context
     * @param playerId id of the player
     * @return stats found
     */
    ArrayList<DStat> getStatsByPlayerId( Context context, long playerId );

    /**
     *
     * @param context application context
     * @param participantId id of the participant
     * @return stats found
     */
    ArrayList<DStat> getStatsByParticipantId( Context context, long participantId );

    /**
     *
     * @param context application context
     * @param tournamentId id of the tournament
     * @return stats found
     */
    ArrayList<DStat> getStatsByTournamentId( Context context, long tournamentId );

    /**
     *
     * @param context application context
     * @param competitionId id of the competition
     * @return stats found
     */
    ArrayList<DStat> getStatsByCompetitionId( Context context, long competitionId );
}
