package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public interface IScoredMatchManager {

    ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId);
    ScoredMatch getById(Context context, long Id);
}
