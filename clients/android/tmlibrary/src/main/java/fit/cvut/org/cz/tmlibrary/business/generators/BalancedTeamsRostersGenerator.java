package fit.cvut.org.cz.tmlibrary.business.generators;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.generators.helpers.MapComparator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamsRostersGenerator;

/**
 * Created by kevin on 2.9.2016.
 */
public class BalancedTeamsRostersGenerator implements ITeamsRostersGenerator {
    @Override
    public boolean generateRosters(List<Team> teams, Map<Long, Player> players, Map<Long, Double> stats) {
        if (teams.isEmpty())
            return false;

        for (Team t : teams)
            if (!t.getPlayers().isEmpty())
                return false;

        Collections.shuffle(teams, new Random(System.nanoTime()));
        int team_index = 0;
        boolean direction_forward = true;

        Map<Long, Double> sortedMap = MapComparator.sortByValue(stats);
        for (Map.Entry<Long, Double> e : sortedMap.entrySet()) {
            // Some players from competition don't have to be in this tournament
            if (players.get(e.getKey()) == null) {
                continue;
            }

            teams.get(team_index).addPlayer(players.get(e.getKey()));

            if (team_index < teams.size()-1 && direction_forward) {
                team_index++;
            } else if (team_index == teams.size()-1 && direction_forward) {
                direction_forward = false;
            } else if (team_index == 0 && !direction_forward) {
                direction_forward = true;
            } else if (team_index <= teams.size()-1 && !direction_forward) {
                team_index--;
            }
        }

        return true;
    }
}
