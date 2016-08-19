package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public interface IMatchDAO {

    /**
     * Insert new match to database
     * @param context application context
     * @param match match to be inserted
     * @return id of inserted match
     */
    long insert(Context context, DMatch match);

    /**
     * Update existing match
     * @param context application context
     * @param match match to be updated
     */
    void update(Context context, DMatch match);

    /**
     * Delete match from database
     * @param context application context
     * @param id id of the match to be deleted
     */
    void delete(Context context, long id);

    /**
     * Get matches by its tournament id
     * @param context application context
     * @param tournamentId id of the tournament
     * @return found matches
     */
    ArrayList<DMatch> getByTournamentId( Context context, long tournamentId );


    /**
     * Get match by its id
     * @param context application context
     * @param id id of the match to be found
     * @return found match
     */
    DMatch getById( Context context, long id );

}
