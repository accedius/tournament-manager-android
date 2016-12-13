package fit.cvut.org.cz.hockey.business;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.hockey.business.managers.interfaces.IHockeyStatisticsManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IPointConfigurationManager;
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
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.managers.CorePlayerManager;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ManagerFactory {
    private static ManagerFactory instance;
    private String name;
    public HockeyDBHelper sportDBHelper;

    public ICorePlayerManager corePlayerManager;
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
            instance.setManagers(context);
        }
        if (!requestedContext.equals(instance.sportDBHelper.getDBName())) {
            Log.d("ERROR", "Requested context "+requestedContext+", got "+instance.sportDBHelper.getDBName());
        }
        return instance;
    }

    private void setManagers(Context context) {
        corePlayerManager = new CorePlayerManager(context);
        competitionManager = new CompetitionManager(context, corePlayerManager, sportDBHelper);
        tournamentManager = new TournamentManager(context, corePlayerManager, sportDBHelper);
        teamManager = new TeamManager(context, corePlayerManager, sportDBHelper);
        matchManager = new MatchManager(context, corePlayerManager, sportDBHelper);
        participantManager = new ParticipantManager(context, corePlayerManager, sportDBHelper);
        participantStatManager = new ParticipantStatManager(context, corePlayerManager, sportDBHelper);
        statisticsManager = new StatisticsManager(context, corePlayerManager, sportDBHelper);
        pointConfigManager = new PointConfigurationManager(context, corePlayerManager, sportDBHelper);
        playerStatManager = new PlayerStatManager(context, corePlayerManager, sportDBHelper);
    }

    private ManagerFactory(Context context) {
        name = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        sportDBHelper = new HockeyDBHelper(context, name);
    }

}
