package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class CompetitionManager implements ICompetitionManager {
    protected abstract Dao<Competition, Long> getCompetitionDao(Context context);

    @Override
    public long insert(Context context, Competition competition) throws SQLException {
        // TODO check if id is filled
        getCompetitionDao(context).create(competition);
        return competition.getId();
    }

    @Override
    public void update(Context context, Competition competition) throws SQLException {
        getCompetitionDao(context).update(competition);
    }

    @Override
    public boolean delete(Context context, long id) throws SQLException {
        // TODO delete only if does not have tournaments or players
        getCompetitionDao(context).delete(getCompetitionDao(context).queryForId(id));
        return true;
    }

    @Override
    public Competition getById(Context context, long id) throws SQLException {
        Competition c = getCompetitionDao(context).queryForId(id);
        c.setType(CompetitionTypes.competitionTypes()[c.getTypeId()]);
        return c;
    }
}
