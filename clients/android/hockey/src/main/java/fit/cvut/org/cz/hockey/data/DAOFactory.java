package fit.cvut.org.cz.hockey.data;

import fit.cvut.org.cz.hockey.data.DAO.MatchDAO;
import fit.cvut.org.cz.hockey.data.DAO.MatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.DAO.PackagePlayerDAO;
import fit.cvut.org.cz.hockey.data.DAO.ParticipantDAO;
import fit.cvut.org.cz.hockey.data.DAO.PointConfigDAO;
import fit.cvut.org.cz.hockey.data.DAO.StatDAO;
import fit.cvut.org.cz.hockey.data.DAO.TeamDAO;
import fit.cvut.org.cz.hockey.data.DAO.TournamentDAO;
import fit.cvut.org.cz.hockey.data.interfaces.IMatchStatisticsDAO;
import fit.cvut.org.cz.hockey.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IStatDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

/**
 * Created by atgot_000 on 31. 3. 2016.
 *
 * factory class for DAO interfaces
 */
public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();

    public ITournamentDAO tournamentDAO = new TournamentDAO();
    public IPackagePlayerDAO packagePlayerDAO = new PackagePlayerDAO();
    public IPointConfigDAO pointConfigDAO = new PointConfigDAO();
    public ITeamDAO teamDAO = new TeamDAO();
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
