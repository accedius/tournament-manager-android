package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ICompetitionDAO {
    /**
     * Insert competition into database
     * @param context application context
     * @param competition competition to be inserted
     */
    void insert(Context context, DCompetition competition);

    /**
     * Update existing competition in database
     * @param context application context
     * @param competition competition to be updated
     */
    void update(Context context, DCompetition competition);

    /**
     * Delete a competition from database
     * @param context application context
     * @param id id of the competition to be deleted
     */
    void delete(Context context, long id);

    /**
     * Get competition by its id
     * @param context application context
     * @param id id of the competition
     * @return found competition
     */
    DCompetition getById(Context context, long id);
}
