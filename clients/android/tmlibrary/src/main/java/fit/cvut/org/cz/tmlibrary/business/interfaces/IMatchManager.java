package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Match;

/**
 * Created by kevin on 9.11.2016.
 */
public interface IMatchManager extends IManager<Match> {
    /**
     * get all matches in tournament
     * @param tournamentId id of the tournament
     * @return found matches
     */
    List<Match> getByTournamentId(long tournamentId);
}
