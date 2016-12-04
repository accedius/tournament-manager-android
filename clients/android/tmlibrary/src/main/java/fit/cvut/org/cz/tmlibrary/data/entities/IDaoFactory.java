package fit.cvut.org.cz.tmlibrary.data.entities;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by kevin on 2.12.2016.
 */
public interface IDaoFactory {
    Dao<Competition, Long> getCompetitionDao();
}
