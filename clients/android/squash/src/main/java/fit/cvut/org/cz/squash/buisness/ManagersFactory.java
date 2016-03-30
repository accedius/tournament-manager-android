package fit.cvut.org.cz.squash.buisness;

import fit.cvut.org.cz.squash.buisness.managers.CompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;

/**
 * Created by Vaclav on 30. 3. 2016.
 */
public class ManagersFactory {
    private static ManagersFactory ourInstance = new ManagersFactory();

    public static ManagersFactory getInstance() {
        return ourInstance;
    }

    private ManagersFactory() {
    }

    public ICompetitionManager competitionManager = new CompetitionManager();
}
