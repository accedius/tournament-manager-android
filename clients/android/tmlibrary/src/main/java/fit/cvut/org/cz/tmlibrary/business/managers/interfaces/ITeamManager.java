package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Interface for Team Manager.
 */
public interface ITeamManager extends IManager<Team> {
    /**
     * get all teams in tournament
     * @param tournamentId id of the tournament
     * @return found teams
     */
    List<Team> getByTournamentId(long tournamentId);

    /**
     * get all players in team
     * @param team team
     * @return found players
     */
    List<Player> getTeamPlayers(Team team);

    /**
     * generates balanced rosters for teams in given tournament
     * @param competitionId id of the competition
     * @param tournamentId id of the tournament
     * @param generatingType type of stat for generating balanced rosters
     * @return boolean true if succeeded, false if failed
     */
    boolean generateRosters(long competitionId, long tournamentId, int generatingType);

    /**
     * Add player to team
     * @param team team
     * @param player player to be addeds
     */
    void addPlayer(Team team, Player player);

    /**
     * Update Team players
     * @param teamId id of the team
     * @param players list of players to be updated
     */
    void updatePlayersInTeam(long teamId, List<Player> players);

    /**
     * Get players which are not in any team in tournament.
     * @param tournamentId id of the tournament
     * @return list of free players
     */
    List<Player> getFreePlayers(long tournamentId);
}
