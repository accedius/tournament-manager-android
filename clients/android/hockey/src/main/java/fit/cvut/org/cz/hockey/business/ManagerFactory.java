package fit.cvut.org.cz.hockey.business;

import fit.cvut.org.cz.hockey.business.interfaces.IPointConfigManager;
import fit.cvut.org.cz.hockey.business.managers.CompetitionManager;
import fit.cvut.org.cz.hockey.business.managers.PackagePlayerManager;
import fit.cvut.org.cz.hockey.business.managers.PointConfigManager;
import fit.cvut.org.cz.hockey.business.managers.StatisticsManager;
import fit.cvut.org.cz.hockey.business.managers.TournamentManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class ManagerFactory {
    private static ManagerFactory instance = new ManagerFactory();

    public ICompetitionManager competitionManager = new CompetitionManager();
    public ITournamentManager tournamentManager = new TournamentManager();
    public StatisticsManager statisticsManager = new StatisticsManager();
    public IPackagePlayerManager packagePlayerManager = new PackagePlayerManager();
    public IPointConfigManager pointConfigManager = new PointConfigManager();

    public static ManagerFactory getInstance()
    {
        return instance;
    }

    private ManagerFactory()
    {

    }


}
