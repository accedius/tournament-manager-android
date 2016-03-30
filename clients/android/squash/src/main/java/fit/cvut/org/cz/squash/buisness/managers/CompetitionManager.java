package fit.cvut.org.cz.squash.buisness.managers;

import android.content.Context;

import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;

/**
 * Created by Vaclav on 30. 3. 2016.
 */
public class CompetitionManager implements ICompetitionManager {
    @Override
    public void insert(Context context, Competition competition) {
        DAOFactory.getInstance().competitionDAO.insert(context, Competition.convertToDCompetition(competition));
    }

    @Override
    public void update(Context context, Competition competition) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public Competition getById(Context context, long id) {
        return null;
    }
}
