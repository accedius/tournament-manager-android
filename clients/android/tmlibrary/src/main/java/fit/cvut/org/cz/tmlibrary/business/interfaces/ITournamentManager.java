package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentManager {

    /**
     * insert new tournament
     * @param context application context
     * @param tournament tournament to be inserted
     */
    void insert(Context context, Tournament tournament);

    /**
     * update existing tournament
     * @param context application context
     * @param tournament tournament to be updated
     */
    void update(Context context, Tournament tournament);

    /**
     * delete tournament
     * @param context application context
     * @param id id of the tournament to be deleted
     * @return true if ok, false if tournament has something and cannot be deleted
     */
    boolean delete(Context context, long id);

    /**
     * get tournament by its id
     * @param context application context
     * @param id id of the tournament to be found
     * @return found tournament
     */
    Tournament getById(Context context, long id);

    /**
     * get all tournaments in competition
     * @param context application context
     * @param competitionId id of the competition
     * @return found tournaments
     */
    ArrayList<Tournament> getByCompetitionId(Context context, long competitionId);
}
