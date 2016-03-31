package fit.cvut.org.cz.hockey.business;

import fit.cvut.org.cz.hockey.business.managers.CompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class ManagerFactory {
    private static ManagerFactory instance = new ManagerFactory();

    public ICompetitionManager competitionManager = new CompetitionManager();

    public static ManagerFactory getInstance()
    {
        return instance;
    }

    private ManagerFactory()
    {

    }


}
