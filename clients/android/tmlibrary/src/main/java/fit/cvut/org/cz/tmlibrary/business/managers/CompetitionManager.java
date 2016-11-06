package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class CompetitionManager implements ICompetitionManager {
    protected abstract Dao<Competition, Long> getDao(Context context);

    @Override
    public void insert(Context context, Competition competition) {
        // TODO check if id is filled
        try {
            getDao(context).create(competition);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, Competition competition) {
        try {
            getDao(context).update(competition);
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
    public List<Competition> getAll(Context context) {
        try {
            return getDao(context).queryForAll();
        } catch (SQLException e) {
            return new ArrayList<Competition>();
        }
    }

    @Override
    public Competition getById(Context context, long id) {
        Competition c = null;
        try {
            c = getDao(context).queryForId(id);
            c.setType(CompetitionTypes.competitionTypes()[c.getTypeId()]);
        } catch (SQLException e) {
            return null;
        }
        return c;
    }
}
