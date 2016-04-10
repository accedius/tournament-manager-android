package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public interface IScoredMatchManager {

    ArrayList<ScoredMatch> getByTournamentId(long tournamentId);
    ScoredMatch getById(long Id);
}
