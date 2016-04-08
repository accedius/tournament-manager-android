package fit.cvut.org.cz.hockey.data;

import fit.cvut.org.cz.hockey.data.DAO.CompetitionDAO;
import fit.cvut.org.cz.hockey.data.DAO.PackagePlayerDAO;
import fit.cvut.org.cz.hockey.data.DAO.TournamentDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class DAOFactory {

    private static DAOFactory instance = new DAOFactory();

    public ICompetitionDAO competitionDAO = new CompetitionDAO();
    public ITournamentDAO tournamentDAO = new TournamentDAO();
    public IPackagePlayerDAO packagePlayerDAO = new PackagePlayerDAO();

    private DAOFactory()
    {

    }

    public static DAOFactory getInstance()
    {
        return instance;
    }

}
