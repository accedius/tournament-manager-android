package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import fit.cvut.org.cz.hockey.data.DAO.CompetitionDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionManager implements ICompetitionManager {

    @Override
    public void insert(Context context, Competition competition) {
        DCompetition dc = Competition.convertToDCompetition(competition);
        DAOFactory.getInstance().competitionDAO.insert( context, dc );
    }

    @Override
    public void update(Context context, Competition competition) {
        DCompetition dc = Competition.convertToDCompetition( competition );
        DAOFactory.getInstance().competitionDAO.update( context, dc );
    }

    @Override
    public void delete(Context context, long id) {
        //TODO odstranit shity pod competitionem
        DAOFactory.getInstance().competitionDAO.delete( context, id );
    }

    @Override
    public Competition getById(Context context, long id) {
        DCompetition dc = DAOFactory.getInstance().competitionDAO.getById(context, id);
        if( dc == null )
            return null;
        Competition c = new Competition( dc );
        return c;
    }
}
