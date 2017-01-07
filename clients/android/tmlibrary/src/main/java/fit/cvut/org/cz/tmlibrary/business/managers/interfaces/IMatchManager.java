package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Match;

/**
 * Interface for Match Manager.
 */
public interface IMatchManager extends IManager<Match> {
    /**
     * get all matches in tournament
     * @param tournamentId id of the tournament
     * @return found matches
     */
    List<Match> getByTournamentId(long tournamentId);
}
