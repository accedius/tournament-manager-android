package fit.cvut.org.cz.squash.business.managers.interfaces;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

/**
 * Created by kevin on 9.12.2016.
 */

public interface IMatchManager extends IManager<Match> {
    /**
     * get all matches in tournament
     * @param tournamentId id of the tournament
     * @return found matches
     */
    List<Match> getByTournamentId(long tournamentId);

    /**
     * generates one round of matches for specified tournament
     * @param tournamentId id of the tournament
     */
    void generateRound(long tournamentId);

    /**
     * resets match, so the match is empty and not played
     * @param matchId id of the match
     */
    void resetMatch(long matchId);

    /**
     * updates match with statistics
     * @param matchId id of the match
     * @param sets list of sets to be added to match
     */
    void updateMatch(long matchId, ArrayList<SetRowItem> sets);
}