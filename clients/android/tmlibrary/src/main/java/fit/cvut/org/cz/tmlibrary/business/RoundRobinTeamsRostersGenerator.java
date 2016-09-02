package fit.cvut.org.cz.tmlibrary.business;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamsRostersGenerator;

/**
 * Created by kevin on 2.9.2016.
 */
public class RoundRobinTeamsRostersGenerator implements ITeamsRostersGenerator {
    @Override
    public void generateRosters(ArrayList<Team> teams, HashMap<Long, Player> players, HashMap<Long, Double> stats) {
        for (Team t : teams)
            if (!t.getPlayers().isEmpty())
                return;

        Collections.shuffle(teams, new Random(System.nanoTime()));
        int team_index = 0;
        boolean direction_forward = true;

        Map<Long, Double> sortedMap = MapComparator.sortByValue(stats);
        for(Map.Entry<Long, Double> e : sortedMap.entrySet()) {
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
    }
}
