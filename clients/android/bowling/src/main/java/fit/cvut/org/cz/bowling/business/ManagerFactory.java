package fit.cvut.org.cz.bowling.business;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.bowling.business.managers.CompetitionManager;
import fit.cvut.org.cz.bowling.business.managers.FrameManager;
import fit.cvut.org.cz.bowling.business.managers.MatchManager;
import fit.cvut.org.cz.bowling.business.managers.ParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.ParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.PlayerStatManager;
import fit.cvut.org.cz.bowling.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.bowling.business.managers.RollManager;
import fit.cvut.org.cz.bowling.business.managers.StatisticManager;
import fit.cvut.org.cz.bowling.business.managers.TeamManager;
import fit.cvut.org.cz.bowling.business.managers.TournamentManager;
import fit.cvut.org.cz.bowling.business.managers.WinConditionManager;
import fit.cvut.org.cz.bowling.data.BowlingDAOFactory;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.presentation.BowlingPackage;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.PackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Manager factory for all the classes in the package
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.ManagerFactory {
    private static Context context;
    private static IManagerFactory instance;
    private static Map<String, IDAOFactory> helpersMap = new HashMap<>();

    @Override
    public <M extends IManager<E>, E extends IEntity> M getEntityManager(Class<E> entity) {
        BaseManager<? extends IEntity> manager = null;
        String entityName = entity.getName();
        
        // Competition
        if (entityName.equals(Competition.class.getName())) {
            manager = new CompetitionManager();
        }
        // Tournament
        else if (entityName.equals(Tournament.class.getName())) {
            manager = new TournamentManager();
        }
        // Team
        else if (entityName.equals(Team.class.getName())) {
            manager = new TeamManager();
        }
        // PointConfiguration
        else if (entityName.equals(PointConfiguration.class.getName())) {
            manager = new PointConfigurationManager();
        }
        // Match
        else if (entityName.equals(Match.class.getName())) {
            manager = new MatchManager();
        }
        //Frame
        else if (entityName.equals(Frame.class.getName())) {
            manager = new FrameManager();
        }
        //Roll
        else if (entityName.equals(Roll.class.getName())) {
            manager = new RollManager();
        }
        // Participant
        else if (entityName.equals(Participant.class.getName())) {
            manager = new ParticipantManager();
        }
        // ParticipantStat
        else if (entityName.equals(ParticipantStat.class.getName())) {
            manager = new ParticipantStatManager();
        }
        // PlayerStat
        else if (entityName.equals(PlayerStat.class.getName())) {
            manager = new PlayerStatManager();
        }
        // Statistic
        else if (entityName.equals(AggregatedStatistics.class.getName())) {
            manager = new StatisticManager();
        }
        // Core Player
        else if (entityName.equals(Player.class.getName())) {
            return (M) new PackagePlayerManager(context);
        }
        // Win Condition
        else if (entityName.equals(WinCondition.class.getName())) {
            manager = new WinConditionManager();
        }

        if (manager == null)
            return null;

        manager.setManagerFactory(this);
        return (M) manager;
    }

    private ManagerFactory() {}

    @Override
    public IDAOFactory getDaoFactory() {
        String name = ((BowlingPackage) context.getApplicationContext()).getSportContext();
        if (!helpersMap.containsKey(name))
            helpersMap.put(name, new BowlingDAOFactory(context, name));
        return helpersMap.get(name);
    }

    public static IManagerFactory getInstance(Context c) {
        context = c;
        if (instance == null)
            instance = new ManagerFactory();
        return instance;
    }

    public static IManagerFactory getInstance() {
        if (instance == null)
            instance = new ManagerFactory();
        return instance;
    }

    public static void reset() {
        instance = null;
        helpersMap.clear();
    }
}
