package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;

/**
 * Created by kevin on 2.9.2016.
 */
public interface ITeamsRostersGenerator {
    /**
     * generates maximally balanced rosters according to given stat
     * @param teams list of teams
     * @param players hashmap of player ids and players
     * @param stats map of players id and stat for balanced rosters
     */
    boolean generateRosters(ArrayList<Team> teams, HashMap<Long, Player> players, HashMap<Long, Double> stats);
}
