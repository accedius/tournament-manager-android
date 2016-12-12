package fit.cvut.org.cz.hockey.business;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.hockey.business.interfaces.IHockeyStatisticsManager;
import fit.cvut.org.cz.hockey.business.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.business.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.hockey.business.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.hockey.business.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.hockey.business.managers.CompetitionManager;
import fit.cvut.org.cz.hockey.business.managers.MatchManager;
import fit.cvut.org.cz.hockey.business.managers.ParticipantManager;
import fit.cvut.org.cz.hockey.business.managers.ParticipantStatManager;
import fit.cvut.org.cz.hockey.business.managers.PlayerStatManager;
import fit.cvut.org.cz.hockey.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.hockey.business.managers.StatisticsManager;
import fit.cvut.org.cz.hockey.business.managers.TeamManager;
import fit.cvut.org.cz.hockey.business.managers.TournamentManager;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;

/**
 * Created by atgot_000 on 31. 3. 2016.
 *
 * factory class returning managers
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.interfaces.ManagerFactory {
    private static ManagerFactory instance;
    private String name;
    public HockeyDBHelper sportDBHelper;

    public ICompetitionManager competitionManager;
    public ITournamentManager tournamentManager;
    public ITeamManager teamManager;
    public IHockeyStatisticsManager statisticsManager;
    public IPointConfigurationManager pointConfigManager;
    public IParticipantManager participantManager;
    public IParticipantStatManager participantStatManager;
    public IPlayerStatManager playerStatManager;
    public IMatchManager matchManager;

    public static ManagerFactory getInstance(Context context) {
        String requestedContext = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        if (instance == null || !instance.sportDBHelper.getDBName().equals(requestedContext)) {
            instance = new ManagerFactory(context);
            instance.setManagers();
        }
        if (!requestedContext.equals(instance.sportDBHelper.getDBName())) {
            Log.d("ERROR", "Requested context "+requestedContext+", got "+instance.sportDBHelper.getDBName());
        }
        return instance;
    }

    private void setManagers() {
        competitionManager = new CompetitionManager(corePlayerManager, sportDBHelper);
        tournamentManager = new TournamentManager(corePlayerManager, sportDBHelper);
        teamManager = new TeamManager(corePlayerManager, sportDBHelper);
        matchManager = new MatchManager(corePlayerManager, sportDBHelper);
        participantManager = new ParticipantManager(corePlayerManager, sportDBHelper);
        participantStatManager = new ParticipantStatManager(corePlayerManager, sportDBHelper);
        statisticsManager = new StatisticsManager(corePlayerManager, sportDBHelper);
        pointConfigManager = new PointConfigurationManager(corePlayerManager, sportDBHelper);
        playerStatManager = new PlayerStatManager(corePlayerManager, sportDBHelper);
    }

    private ManagerFactory(Context context, String name) {
        sportDBHelper = new HockeyDBHelper(context, name);
    }

    private ManagerFactory(Context context) {
        name = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        sportDBHelper = new HockeyDBHelper(context, name);
    }

}
