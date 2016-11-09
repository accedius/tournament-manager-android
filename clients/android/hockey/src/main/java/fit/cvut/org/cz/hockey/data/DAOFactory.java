package fit.cvut.org.cz.hockey.data;

import fit.cvut.org.cz.hockey.data.DAO.MatchDAO;
import fit.cvut.org.cz.hockey.data.DAO.MatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.DAO.PackagePlayerDAO;
import fit.cvut.org.cz.hockey.data.DAO.ParticipantDAO;
import fit.cvut.org.cz.hockey.data.DAO.StatDAO;
import fit.cvut.org.cz.hockey.data.interfaces.IMatchStatisticsDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IStatDAO;

/**
 * Created by atgot_000 on 31. 3. 2016.
 *
 * factory class for DAO interfaces
 */
public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();

    public IPackagePlayerDAO packagePlayerDAO = new PackagePlayerDAO();
    public IMatchDAO matchDAO = new MatchDAO();
    public IParticipantDAO participantDAO = new ParticipantDAO();
    public IMatchStatisticsDAO matchStatisticsDAO = new MatchStatisticsDAO();
    public IStatDAO statDAO = new StatDAO();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return instance;
    }

}
