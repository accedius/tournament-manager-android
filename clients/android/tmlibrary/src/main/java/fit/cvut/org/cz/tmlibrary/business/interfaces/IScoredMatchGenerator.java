package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface IScoredMatchGenerator {
    /**
     * generates round robin of matches
     * @param participants participants to be divided into teams
     * @param round number of round to be generated
     * @return list of matches
     */
    List<Match> generateRound(List<Participant> participants, int round);
}
