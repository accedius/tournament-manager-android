package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public interface ITeamDAO {
    /**
     * insert team into database
     * @param context application context
     * @param team team to be inserted
     * @return id of inserted team
     */
    long insert(Context context, DTeam team);

    /**
     * update team in database
     * @param context application context
     * @param team team to be updated
     */
    void update(Context context, DTeam team);

    /**
     * delete team from database
     * @param context application context
     * @param id id of the team to be deleted
     */
    void delete(Context context, long id);

    /**
     * get team by id
     * @param context application context
     * @param id id of the team to be get
     * @return found team
     */
    DTeam getById(Context context, long id);

    /**
     * get all teams in tournament
     * @param context application context
     * @param tournamentId id of the tournament
     * @return found teams
     */
    ArrayList<DTeam> getByTournamentId(Context context, long tournamentId);

}
