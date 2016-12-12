package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;

/**
 * Created by kevin on 9.12.2016.
 */

public interface ISquashStatisticManager {
    /**
     *
     * @param context application context
     * @return aggregated statistics for all players in application throughout all competitions
     */
    List<SAggregatedStats> getAllAggregated(Context context);

    /**
     *
     * @param context application context
     * @param playerId id of player
     * @return aggregated statistics for one player in application throughout all competitions
     */
    SAggregatedStats getByPlayerId(Context context, long playerId);

    /**
     *
     * @param context application context
     * @param compId id of competition
     * @return aggregated statistics for players in competition
     */
    List<SAggregatedStats> getByCompetitionId(Context context, long compId);

    /**
     * Get information about Sets for Match
     * @param context application context
     * @param matchId id of match
     * @return list of set info
     */
    List<SetRowItem> getMatchSets(Context context, long matchId);

    /**
     *
     * @param context application context
     * @param tourId id of tournament
     * @return aggregated statistics for players in tournament
     */
    List<SAggregatedStats> getByTournamentId(Context context, long tourId);

    /**
     *
     * @param context application context
     * @param tourId id of tournament
     * @return standings of teams participating in tournament. Ordered by point
     */
    List<StandingItem> getStandingsByTournamentId(Context context, long tourId);
}
