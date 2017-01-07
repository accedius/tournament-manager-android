package fit.cvut.org.cz.tournamentmanager.business.generators;

import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.generators.BalancedTeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamsRostersGenerator;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;

/**
 * Created by kevin on 5.1.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BalancedTeamRostersGeneratorTest extends AndroidTestCase {
    ITeamsRostersGenerator teamsRostersGenerator = null;

    @Before
    public void setUp() {
        teamsRostersGenerator = new BalancedTeamsRostersGenerator();
        assertNotNull(teamsRostersGenerator);
    }

    @Test
    public void verifyNoTeams() {
        List<Team> teams = new ArrayList<>();
        Map<Long, Player> playersMap = new HashMap<>();
        Map<Long, Double> statsMap = new HashMap<>();
        assertFalse(teamsRostersGenerator.generateRosters(teams, playersMap, statsMap));
    }

    @Test
    public void verifyTeamContainsPlayer() {
        List<Team> teams = new ArrayList<>();
        Team team = new Team(1, "Team A");
        team.addPlayer(new Player(1, "Player 1", "", ""));
        teams.add(team);
        Map<Long, Player> playersMap = new HashMap<>();
        Map<Long, Double> statsMap = new HashMap<>();
        assertFalse(teamsRostersGenerator.generateRosters(teams, playersMap, statsMap));
    }

    @Test
    public void verifyRostersSize() {
        List<Team> teams = new ArrayList<>();
        Team team1 = new Team(1, "Team A");
        Team team2 = new Team(1, "Team B");
        Team team3 = new Team(1, "Team C");
        Team team4 = new Team(1, "Team D");
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        teams.add(team4);

        Map<Long, Player> playersMap = new HashMap<>();
        playersMap.put(1L, new Player(1, "Player 1", "", ""));
        playersMap.put(2L, new Player(2, "Player 2", "", ""));
        playersMap.put(3L, new Player(3, "Player 3", "", ""));
        playersMap.put(4L, new Player(4, "Player 4", "", ""));
        playersMap.put(5L, new Player(5, "Player 5", "", ""));
        playersMap.put(6L, new Player(6, "Player 6", "", ""));
        playersMap.put(7L, new Player(7, "Player 7", "", ""));
        playersMap.put(8L, new Player(8, "Player 8", "", ""));

        Map<Long, Double> statsMap = new HashMap<>();
        statsMap.put(1L, 8D);
        statsMap.put(2L, 7D);
        statsMap.put(3L, 6D);
        statsMap.put(4L, 5D);
        statsMap.put(5L, 4D);
        statsMap.put(6L, 3D);
        statsMap.put(7L, 2D);
        statsMap.put(8L, 1D);
        assertTrue(teamsRostersGenerator.generateRosters(teams, playersMap, statsMap));

        /* Verify each team has two players */
        assertEquals(2, team1.getPlayers().size());
        assertEquals(2, team2.getPlayers().size());
        assertEquals(2, team3.getPlayers().size());
        assertEquals(2, team4.getPlayers().size());

        /* Verify player 1 is in team with 8, 2 with 7, and so on */
        for (Team team : teams) {
            Player firstPlayer = team.getPlayers().get(0);
            Player secondPlayer = team.getPlayers().get(1);
            assertEquals(9, firstPlayer.getId() + secondPlayer.getId());
        }
    }

}
