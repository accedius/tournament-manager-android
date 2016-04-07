package fit.cvut.org.cz.squash.buisness;

import fit.cvut.org.cz.squash.buisness.interfaces.IStatsManager;
import fit.cvut.org.cz.squash.buisness.managers.CompetitionManager;
import fit.cvut.org.cz.squash.buisness.managers.PlayerManager;
import fit.cvut.org.cz.squash.buisness.managers.StatsManager;
import fit.cvut.org.cz.squash.buisness.managers.TournamentManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;

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
    public ITournamentManager tournamentManager = new TournamentManager();
    public IPackagePlayerManager playerManager = new PlayerManager();
    public IStatsManager statsManager = new StatsManager();
}
