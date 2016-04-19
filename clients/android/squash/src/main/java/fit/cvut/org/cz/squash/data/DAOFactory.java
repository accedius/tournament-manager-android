package fit.cvut.org.cz.squash.data;

import fit.cvut.org.cz.squash.data.daos.CompetitionDAO;
import fit.cvut.org.cz.squash.data.daos.PlayerDAO;
import fit.cvut.org.cz.squash.data.daos.PointConfigDAO;
import fit.cvut.org.cz.squash.data.daos.TeamDAO;
import fit.cvut.org.cz.squash.data.daos.TournamentDAO;
import fit.cvut.org.cz.squash.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

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
    public ITournamentDAO tournamentDAO = new TournamentDAO();
    public IPackagePlayerDAO playerDAO = new PlayerDAO();
    public ITeamDAO teamDAO = new TeamDAO();
    public IPointConfigDAO pointCfgDAO = new PointConfigDAO();
}
