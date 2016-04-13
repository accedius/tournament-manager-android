package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface IScoredMatchGenerator {

    ArrayList<ScoredMatch> generateRound(ArrayList<Participant> participants, int round);
}
