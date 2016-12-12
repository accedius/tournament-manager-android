package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 9.12.2016.
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
     * updates match with statistics
     * @param context application context
     * @param matchId id of the match
     * @param sets list of sets to be added to match
     */
    void updateMatch(Context context, long matchId, ArrayList<SetRowItem> sets);
}