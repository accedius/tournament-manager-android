package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.MatchParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

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
    ArrayList<ScoredMatch> generateRound(ArrayList<MatchParticipant> participants, int round);
}
