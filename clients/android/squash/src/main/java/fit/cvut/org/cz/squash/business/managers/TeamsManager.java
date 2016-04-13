package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamsManager implements ITeamManager {
    @Override
    public void insert(Context context, Team team) {

    }

    @Override
    public void update(Context context, Team team) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public Team getById(Context context, long id) {
        return null;
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {

        //TODO remove mock data
        ArrayList<Team> teams = new ArrayList<>();

        Team t = new Team();
        t.setName("team1");
        t.setPlayers(new ArrayList<Player>());
        t.getPlayers().add(new Player(0, "peter", null, null));
        t.getPlayers().add(new Player(0, "Jana", null, null));
        t.getPlayers().add(new Player(0, "Kenny", null, null));
        teams.add(t);

        t = new Team();
        t.setName("team2");
        t.setPlayers(new ArrayList<Player>());
        teams.add(t);

        t = new Team();
        t.setName("team Chuck");
        t.setPlayers(new ArrayList<Player>());
        t.getPlayers().add(new Player(0, "Chuck Norris", null, null));

        teams.add(t);


        return teams;
    }
}
