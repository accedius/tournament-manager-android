package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

/**
 * Interface for Matches generating.
 */
public interface IMatchGenerator {
    /**
     * generates round robin of matches
     * @param participants participants to be divided into teams
     * @param round number of round to be generated
     * @return list of matches
     */
    List<Match> generateRound(List<Participant> participants, int round);
}
