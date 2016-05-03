package fit.cvut.org.cz.hockey.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.hockey.data.entities.DMatchStat;

/**
 * Created by atgot_000 on 3. 5. 2016.
 *
 * DAO for the match statistic entity
 */
public interface IMatchStatisticsDAO {

    /**
     * Creates new match statistics in database
     * @param context
     * @param stat stat to be created
     * @return id of inserted stat
     */
    long createStatsForMatch( Context context, DMatchStat stat);

    /**
     *
     * @param context
     * @param matchId id of match
     * @return statistics of the requested match
     */
    DMatchStat getByMatchId( Context context, long matchId);

    /**
     *
     * @param context
     * @param matchStat stat to be updated
     */
    void update( Context context, DMatchStat matchStat );

    /**
     * deletes the statistic of a given match
     * @param context
     * @param matchId id of match
     */
    void delete(Context context, long matchId);
}
