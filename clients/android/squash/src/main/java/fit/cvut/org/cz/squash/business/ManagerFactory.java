package fit.cvut.org.cz.squash.business;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.squash.business.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.business.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.business.interfaces.ISquashStatisticManager;
import fit.cvut.org.cz.squash.business.managers.CompetitionManager;
import fit.cvut.org.cz.squash.business.managers.MatchManager;
import fit.cvut.org.cz.squash.business.managers.ParticipantManager;
import fit.cvut.org.cz.squash.business.managers.ParticipantStatManager;
import fit.cvut.org.cz.squash.business.managers.PlayerStatManager;
import fit.cvut.org.cz.squash.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.squash.business.managers.StatisticManager;
import fit.cvut.org.cz.squash.business.managers.TeamManager;
import fit.cvut.org.cz.squash.business.managers.TournamentManager;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.managers.CorePlayerManager;

/**
 * Created by Vaclav on 30. 3. 2016.
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.interfaces.ManagerFactory {
    private static ManagerFactory instance;
    private String name;
    public SquashDBHelper sportDBHelper;

    public ICorePlayerManager corePlayerManager;
    public ICompetitionManager competitionManager;
    public ITournamentManager tournamentManager;
    public ITeamManager teamManager;
    public ISquashStatisticManager statisticManager;
    public IPointConfigurationManager pointConfigManager;
    public fit.cvut.org.cz.tmlibrary.business.interfaces.IParticipantManager participantManager;
    public IParticipantStatManager participantStatManager;
    public IPlayerStatManager playerStatManager;
    public IMatchManager matchManager;

    public static ManagerFactory getInstance(Context context) {
        String requestedContext = ((SquashPackage) context.getApplicationContext()).getSportContext();
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
        statisticManager = new StatisticManager(context, corePlayerManager, sportDBHelper);
        pointConfigManager = new PointConfigurationManager(context, corePlayerManager, sportDBHelper);
        playerStatManager = new PlayerStatManager(context, corePlayerManager, sportDBHelper);
    }

    private ManagerFactory(Context context) {
        name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        sportDBHelper = new SquashDBHelper(context, name);
    }

}
