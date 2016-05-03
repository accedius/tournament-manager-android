package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamsManager implements ITeamManager {
    @Override
    public void insert(Context context, Team team) {
        DAOFactory.getInstance().teamDAO.insert(context, Team.convertToDTeam(team));
    }

    @Override
    public void update(Context context, Team team) {
        DAOFactory.getInstance().teamDAO.update(context, Team.convertToDTeam(team));
    }

    @Override
    public boolean delete(Context context, long id) {
        Team t = getById(context, id);
        if (t.getPlayers().size() != 0) return false;
        if (DAOFactory.getInstance().participantDAO.getParticipantsByTeamId(context, id).size() != 0) return false;
        DAOFactory.getInstance().teamDAO.delete(context, id);
        return true;
    }

    @Override
    public Team getById(Context context, long id) {

        Team t = new Team(DAOFactory.getInstance().teamDAO.getById(context, id));
        t.setPlayers(ManagersFactory.getInstance().playerManager.getPlayersByTeam(context, t.getId()));

        return t;
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {

        ArrayList<Team> teams = new ArrayList<>();
        ArrayList<DTeam> dTeams = DAOFactory.getInstance().teamDAO.getByTournamentId(context, tournamentId);
        for (DTeam dTeam : dTeams) {
            Team t = new Team(dTeam);
            t.setPlayers(ManagersFactory.getInstance().playerManager.getPlayersByTeam(context, t.getId()));
            teams.add(t);
        }

        return teams;
    }
}
