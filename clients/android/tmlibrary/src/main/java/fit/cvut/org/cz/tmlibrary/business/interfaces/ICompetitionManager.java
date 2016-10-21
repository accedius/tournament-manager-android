package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ICompetitionManager {
    /**
     * insert new competition
     * @param context application context
     * @param competition competition to be inserted
     * @return id of inserted competition
     */
    Long insert(Context context, Competition competition);

    /**
     * update competition
     * @param context application context
     * @param competition competition to be updated
     */
    void update(Context context, Competition competition);

    /**
     * delete competition from app
     * @param context application context
     * @param id id of competition to be deleted
     * @return true of competition is deleted, false if competition contains something and thus cannot be deleted
     */
    boolean delete(Context context, long id);

    /**
     * get competition by its id
     * @param context application context
     * @param id id of the competition
     * @return found competition
     */
    Competition getById(Context context, long id);
}
