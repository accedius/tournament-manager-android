package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.tmlibrary.business.interfaces.*;
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
    AggregatedStatistics getByPlayerID(Context context, long playerId);

    /**
     *
     * @param context application context
     * @param compId id of competition
     * @return aggregated statistics for players in competition
     */
    List<AggregatedStatistics> getByCompetitionID(Context context, long compId);

    /**
     *
     * @param context application context
     * @param tourId id of tournament
     * @return aggregated statistics for players in tournament
     */
    List<AggregatedStatistics> getByTournamentID(Context context, long tourId);

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
     * @param id id of match
     * @return entity Match containing overtime and shootouts
     */
    Match getMatchScoreByMatchId(Context context, long id);

    /**
     * Sets the score of a match and gives all players the correct outcome
     * @param context application context
     * @param id id of match
     * @param score Match entity containing the score and overtime and shootouts
     */
    void setMatchScoreByMatchId(Context context, long id, Match score);

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
