package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 28.11.2016.
 */
public interface IMatchManager extends IManager<Match> {
    /**
     * get all matches in tournament
     * @param context application context
     * @param tournamentId id of the tournament
     * @return found matches
     */
    List<Match> getByTournamentId(Context context, long tournamentId);

    /**
     * generates one round of matches for specified tournament
     * @param context application context
     * @param tournamentId id of the tournament
     */
    void generateRound(Context context, long tournamentId);

    /**
     * resets match, so the match is empty and not played
     * @param context application context
     * @param matchId id of the match
     */
    void resetMatch(Context context, long matchId);

    /**
     * begins match, sets played to true
     * @param context application context
     * @param matchId id of the match
     */
    void beginMatch(Context context, long matchId);
}
