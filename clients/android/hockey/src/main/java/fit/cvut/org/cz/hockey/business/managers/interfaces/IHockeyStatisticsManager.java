package fit.cvut.org.cz.hockey.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.Standing;

/**
 * Created by atgot_000 on 3. 5. 2016.
 * Interface for manager, that works with all kinds of hockey statistics
 */
public interface IHockeyStatisticsManager {
    /**
     *
     * @param playerId id of player
     * @return aggregated statistics for one player in application throughout all competitions
     */
    AggregatedStatistics getByPlayerId(long playerId);

    /**
     *
     * @param compId id of competition
     * @return aggregated statistics for players in competition
     */
    List<AggregatedStatistics> getByCompetitionId(long compId);

    /**
     *
     * @param tourId id of tournament
     * @return aggregated statistics for players in tournament
     */
    List<AggregatedStatistics> getByTournamentId(long tourId);

    /**
     *
     * @param tourId id of tournament
     * @return standings of teams participating in tournament. Ordered by point
     */
    List<Standing> getStandingsByTournamentId(long tourId);
}
