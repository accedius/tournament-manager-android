package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {

    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {
        return null;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {
        return null;
    }
}
