package fit.cvut.org.cz.hockey.data;

import fit.cvut.org.cz.hockey.data.DAO.CompetitionDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class DAOFactory {

    private static DAOFactory instance = new DAOFactory();

    public ICompetitionDAO competitionDAO = new CompetitionDAO();

    private DAOFactory()
    {

    }

    public static DAOFactory getInstance()
    {
        return instance;
    }

}
