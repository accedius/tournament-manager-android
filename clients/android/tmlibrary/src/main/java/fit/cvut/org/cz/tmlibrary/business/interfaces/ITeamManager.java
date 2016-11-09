package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Team;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public interface ITeamManager extends IManager<Team> {
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
