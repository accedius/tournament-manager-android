package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentDAO {

    /**
     * insert tournament into database
     * @param context application context
     * @param tournament tournament to be inserted
     * @return id of inserted tournament
     */
    long insert(Context context, DTournament tournament);

    /**
     * update tournament in database
     * @param context application context
     * @param tournament tournament to be updated
     */
    void update(Context context, DTournament tournament);

    /**
     * delete tournament from database
     * @param context application context
     * @param id id of tournament to be deleted
     */
    void delete(Context context, long id);

    /**
     * find tournament by its id
     * @param context application context
     * @param id id of tournament to be found
     * @return found tournament
     */
    DTournament getById(Context context, long id);

    /**
     * get all tournaments in competition
     * @param context application context
     * @param competitionId id of competition
     * @return found tournaments
     */
    ArrayList<DTournament> getByCompetitionId(Context context, long competitionId);
}
