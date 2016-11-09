package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 9.11.2016.
 */
abstract public class TeamManager implements ITeamManager {
    protected IPackagePlayerManager packagePlayerManager;
    protected abstract Dao<Team, Long> getDao(Context context);

    public TeamManager(IPackagePlayerManager packagePlayerManager) {
        this.packagePlayerManager = packagePlayerManager;
    }

    @Override
    public void insert(Context context, Team team) {
        // TODO check if id is filled
        try {
            getDao(context).create(team);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, Team team) {
        try {
            getDao(context).update(team);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(Context context, long id) {
        // TODO delete only if does not have matches or players
        try {
            getDao(context).delete(getDao(context).queryForId(id));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Team getById(Context context, long id) {
        try {
            Team t = getDao(context).queryForId(id);
            t.setPlayers(packagePlayerManager.getPlayersByTeam(context, id));
            return t;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Team> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<Team>();
        }
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {
        ArrayList<Team> res = new ArrayList<>();
        try {
            List<Team> teams = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .query();
            res.addAll(teams);
            for (Team t : teams) {
                t.setPlayers(packagePlayerManager.getPlayersByTeam(context, t.getId()));
            }
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    @Override
    public boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType) {
        return false;
    }
}
