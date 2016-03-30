package fit.cvut.org.cz.squash.data;

import fit.cvut.org.cz.squash.data.daos.CompetitionDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public class DAOFactory {

    private static DAOFactory ourInstance = new DAOFactory();

    public static DAOFactory getInstance() {
        return ourInstance;
    }

    private DAOFactory() {
    }

    public ICompetitionDAO competitionDAO = new CompetitionDAO();
}
