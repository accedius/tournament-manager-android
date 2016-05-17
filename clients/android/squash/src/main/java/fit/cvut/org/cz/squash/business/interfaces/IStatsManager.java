package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public interface IStatsManager {
    /**
     *
     * @param context
     * @param competitionId
     * @return List of statistics that are counted form all tournaments in given competition.
     * One item of returned list holds computed statistics for one player registered in given competition
     */
    ArrayList<SAggregatedStats> getAggregatedStatsByCompetitionId(Context context, long competitionId);
    /**
     *
     * @param context
     * @param tournamentId
     * @return List of statistics that are counted for given tournament.
     * One item of returned list holds computed statistics for one player registered in tournament competition
     */
    ArrayList<SAggregatedStats> getAggregatedStatsByTournamentId(Context context, long tournamentId);
    /**
     *
     * @param context
     * @param playerID
     * @return List of statistics that are counted form all competitions for given player.
     * One item of returned list holds computed statistics for one player.
     * List size is always 1
     */
    ArrayList<SAggregatedStats> getAggregatedStatsByPlayerId(Context context, long playerID);
    ArrayList<SAggregatedStats> getAllAggregatedStats(Context context);

    /**
     *
     * @param context
     * @param tournamentId
     * @return List of statiscics that describes how well are participants doing in given tournament, this list is sorted that best participant
     * should be first
     */
    ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId);

    /**
     *
     * @param context
     * @param matchId
     * @return List of sets of given match to be displayed to user
     */
    ArrayList<SetRowItem> getSetsForMatch(Context context, long matchId);

    /**
     * Updates sets for given match to match given list
     * @param context
     * @param matchId
     * @param sets
     */
    void updateStatsForMatch(Context context, long matchId, ArrayList<SetRowItem> sets);

    /**
     *
     * @param context
     * @param matchId
     * @param role
     * @return List of players that participate in match with given id and given role. Role can be home or away
     */
    ArrayList<Player> getPlayersForMatch(Context context, long matchId, String role);

}
