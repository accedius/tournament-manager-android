package fit.cvut.org.cz.tmlibrary.data.interfaces;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ICompetitionDAO {

    void insert(Competition competition);
    void update(Competition competition);
    void delete(long id);

    Competition getById(long id);
}
