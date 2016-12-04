package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
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
    List<Team> getByTournamentId(Context context, long tournamentId);

    /**
     * get all players in team
     * @param context application context
     * @param team team
     * @return found players
     */
    List<Player> getTeamPlayers(Context context, Team team);

    /**
     * generates balanced rosters for teams in given tournament
     * @param context application context
     * @param competitionId id of the competition
     * @param tournamentId id of the tournament
     * @param generatingType type of stat for generating balanced rosters
     * @return boolean true if succeeded, false if failed
     */
    boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType);

    /**
     * Add player to team
     * @param context application context
     * @param team team
     * @param player player to be addeds
     */
    void addPlayer(Context context, Team team, Player player);

    /**
     * Update Team players
     * @param context application context
     * @param teamId id of the team
     * @param players list of players to be updated
     */
    void updatePlayersInTeam(Context context, long teamId, List<Player> players);

    /**
     * Get players which are not in any team in tournament.
     * @param context application context
     * @param tournamentId id of the tournament
     * @return list of free players
     */
    List<Player> getFreePlayers(Context context, long tournamentId);
}
