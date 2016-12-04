package fit.cvut.org.cz.hockey.business;

import android.content.Context;

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
        if (instance == null) {
            instance = new ManagerFactory(context);
            instance.competitionManager = new CompetitionManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.tournamentManager = new TournamentManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.teamManager = new TeamManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.matchManager = new MatchManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.participantManager = new ParticipantManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.participantStatManager = new ParticipantStatManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.statisticsManager = new StatisticsManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.pointConfigManager = new PointConfigurationManager(instance.corePlayerManager, instance.sportDBHelper);
            instance.playerStatManager = new PlayerStatManager(instance.corePlayerManager, instance.sportDBHelper);
        }
        return instance;
    }

    private ManagerFactory(Context context) {
        name = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        sportDBHelper = new HockeyDBHelper(context, name);
    }

}
