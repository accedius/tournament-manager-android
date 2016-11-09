package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class TournamentManager implements ITournamentManager {
    protected abstract Dao<Tournament, Long> getDao(Context context);

    @Override
    public void insert(Context context, Tournament tournament) {
        // TODO check if id is filled
        try {
            getDao(context).create(tournament);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, Tournament tournament) {
        try {
            getDao(context).update(tournament);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(Context context, long id) {
        // TODO delete only if does not have tournaments or players
        try {
            getDao(context).delete(getDao(context).queryForId(id));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Tournament> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<Tournament>();
        }
    }

    @Override
    public Tournament getById(Context context, long id) {
        Tournament t = null;
        try {
            t = getDao(context).queryForId(id);
        } catch (SQLException e) {
            return null;
        }
        return t;
    }
}
