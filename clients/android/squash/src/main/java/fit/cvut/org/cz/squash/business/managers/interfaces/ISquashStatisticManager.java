package fit.cvut.org.cz.squash.business.managers.interfaces;

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
     * @param playerId id of player
     * @return aggregated statistics for one player in application throughout all competitions
     */
    SAggregatedStats getByPlayerId(long playerId);

    /**
     *
     * @param compId id of competition
     * @return aggregated statistics for players in competition
     */
    List<SAggregatedStats> getByCompetitionId(long compId);

    /**
     * Get information about Sets for Match
     * @param matchId id of match
     * @return list of set info
     */
    List<SetRowItem> getMatchSets(long matchId);

    /**
     *
     * @param tourId id of tournament
     * @return aggregated statistics for players in tournament
     */
    List<SAggregatedStats> getByTournamentId(long tourId);

    /**
     *
     * @param tourId id of tournament
     * @return standings of teams participating in tournament. Ordered by point
     */
    List<StandingItem> getStandingsByTournamentId(long tourId);
}
