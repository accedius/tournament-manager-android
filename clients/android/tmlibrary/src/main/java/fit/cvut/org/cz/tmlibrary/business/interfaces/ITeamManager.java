package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Team;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public interface ITeamManager {
    /**
     * insert team
     * @param context application context
     * @param team team to be inserted
     * @return id of inserted team
     */
    long insert(Context context, Team team);

    /**
     * update a team (including its rosters)
     * @param context application context
     * @param team team to be updated
     */
    void update(Context context, Team team);

    /**
     * delete a team
     * @param context application context
     * @param id id of the team to be deleted
     * @return true if ok, false if  team is in match and cannot be deleted
     */
    boolean delete(Context context, long id);

    /**
     * get team by its id
     * @param context application context
     * @param id id of the team
     * @return found team
     */
    Team getById(Context context, long id);

    /**
     * get all teams in tournament
     * @param context application context
     * @param tournamentId id of the tournament
     * @return found teams
     */
    ArrayList<Team> getByTournamentId(Context context, long tournamentId);

    /**
     * generates balanced rosters for teams in given tournament
     * @param context application context
     * @param competitionId id of the competition
     * @param tournamentId id of the tournament
     * @param generatingType type of stat for generating balanced rosters
     * @return boolean true if succeeded, false if failed
     */
    boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType);

}
