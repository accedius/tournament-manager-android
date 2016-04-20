package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentManager {

    void insert(Context context, Tournament tournament);
    void update(Context context, Tournament tournament);
    boolean delete(Context context, long id);

    Tournament getById(Context context, long id);
    ArrayList<Tournament> getByCompetitionId(Context context, long competitionId);
}
