package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

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
        DAOFactory.getInstance().competitionDAO.update(context, Competition.convertToDCompetition(competition));
    }

    @Override
    public boolean delete(Context context, long id) {

        //TODO delete everything else before competition

        DAOFactory.getInstance().competitionDAO.delete(context, id);
        return false;
    }

    @Override
    public Competition getById(Context context, long id) {
        DCompetition res = DAOFactory.getInstance().competitionDAO.getById(context, id);
        if( res == null ) return null;
        return new Competition( res );
    }
}
