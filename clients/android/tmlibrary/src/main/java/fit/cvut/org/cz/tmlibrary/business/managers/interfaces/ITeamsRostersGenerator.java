package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Interface for team rosters generating.
 */
public interface ITeamsRostersGenerator {
    /**
     * generates maximally balanced rosters according to given stat
     * @param teams list of teams
     * @param players map of player ids and players
     * @param stats map of players id and stat for balanced rosters
     * @return true if rosters were generated, false otherwise
     */
    boolean generateRosters(List<Team> teams, Map<Long, Player> players, Map<Long, Double> stats);
}
