package fit.cvut.org.cz.tournamentmanager.data;

import fit.cvut.org.cz.tournamentmanager.data.daos.PlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPlayerDAO;

/**
 * Created by kevin on 7. 4. 2016.
 */
public class DAOFactory {

    private static DAOFactory ourInstance = new DAOFactory();

    public static DAOFactory getInstance() {
        return ourInstance;
    }

    private DAOFactory() {
    }

    public IPlayerDAO playerDAO = new PlayerDAO();
}
