package fit.cvut.org.cz.squash.business;

import fit.cvut.org.cz.squash.business.interfaces.IParticipantManager;
import fit.cvut.org.cz.squash.business.interfaces.IPointConfigManager;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.squash.business.managers.CompetitionManager;
import fit.cvut.org.cz.squash.business.managers.MatchManager;
import fit.cvut.org.cz.squash.business.managers.ParticipantManager;
import fit.cvut.org.cz.squash.business.managers.PlayerManager;
import fit.cvut.org.cz.squash.business.managers.PointConfigManager;
import fit.cvut.org.cz.squash.business.managers.StatsManager;
import fit.cvut.org.cz.squash.business.managers.TeamsManager;
import fit.cvut.org.cz.squash.business.managers.TournamentManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
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
    public IScoredMatchManager matchManager = new MatchManager();
    public ITeamManager teamsManager = new TeamsManager();
    public IPointConfigManager pointConfigManager = new PointConfigManager();
    public IParticipantManager participantManager = new ParticipantManager();
}
