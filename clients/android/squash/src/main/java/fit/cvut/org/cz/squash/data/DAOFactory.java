package fit.cvut.org.cz.squash.data;

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
}
