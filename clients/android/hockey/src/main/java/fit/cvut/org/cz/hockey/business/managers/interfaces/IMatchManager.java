package fit.cvut.org.cz.hockey.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

/**
 * Created by kevin on 28.11.2016.
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
     * begins match, sets played to true
     * @param matchId id of the match
     */
    void beginMatch(long matchId);
}
