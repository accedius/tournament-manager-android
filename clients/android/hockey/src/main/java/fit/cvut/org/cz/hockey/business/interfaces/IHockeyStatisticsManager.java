package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

/**
 * Created by atgot_000 on 3. 5. 2016.
 * Interface for manager, that works with all kinds of hockey statistics
 */
public interface IHockeyStatisticsManager {
    /**
     *
     * @param context application context
     * @return aggregated statistics for all players in application throughout all competitions
     */
    List<AggregatedStatistics> getAllAggregated(Context context);

    /**
     *
     * @param context application context
     * @param playerId id of player
     * @return aggregated statistics for one player in application throughout all competitions
     */
    AggregatedStatistics getByPlayerId(Context context, long playerId);

    /**
     *
     * @param context application context
     * @param compId id of competition
     * @return aggregated statistics for players in competition
     */
    List<AggregatedStatistics> getByCompetitionId(Context context, long compId);

    /**
     *
     * @param context application context
     * @param tourId id of tournament
     * @return aggregated statistics for players in tournament
     */
    List<AggregatedStatistics> getByTournamentId(Context context, long tourId);

    /**
     *
     * @param context application context
     * @param tourId id of tournament
     * @return standings of teams participating in tournament. Ordered by point
     */
    List<Standing> getStandingsByTournamentId(Context context, long tourId);

    /**
     *
     * @param context application context
     * @param playerId id of player
     * @param matchId id of match
     * @return statistics of the given player in the given match
     */
    MatchPlayerStatistic getPlayerStatsInMatch(Context context, long playerId, long matchId);

    /**
     * updates players in match - sets the participating players to a given list
     * @param context application context
     * @param matchId id of match
     * @param parType type of participant - Home or Away
     * @param playerIds players to be set as participating
     */
    void updatePlayersInMatch(Context context, long matchId, ParticipantType parType, List<Long> playerIds);

    /**
     * Updates statistics of given player in a match
     * @param context application context
     * @param statistic statistics to be set
     * @param matchId id of match
     */
    void updatePlayerStatsInMatch(Context context, MatchPlayerStatistic statistic, long matchId);

}
