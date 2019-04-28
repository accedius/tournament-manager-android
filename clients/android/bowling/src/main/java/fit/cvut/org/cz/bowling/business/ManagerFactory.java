package fit.cvut.org.cz.bowling.business;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.bowling.data.BowlingDAOFactory;
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

public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.ManagerFactory {
    private static Context context;
    private static IManagerFactory instance;
    private static Map<String, IDAOFactory> helpersMap = new HashMap<>();

    @Override
    public <M extends IManager<E>, E extends IEntity> M getEntityManager(Class<E> entity) {
        BaseManager<? extends IEntity> manager = null;

        // Competition
        if (entity.getName().equals(Competition.class.getName())) {
            manager = new fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager();
        }
        // Tournament
        /*else if (entity.getName().equals(Tournament.class.getName())) {
            manager = new TournamentManager();
        }
        // Team
        else if (entity.getName().equals(Team.class.getName())) {
            manager = new TeamManager();
        }
        // PointConfiguration
        else if (entity.getName().equals(PointConfiguration.class.getName())) {
            manager = new PointConfigurationManager();
        }
        // Match
        else if (entity.getName().equals(Match.class.getName())) {
            manager = new MatchManager();
        }
        // Participant
        else if (entity.getName().equals(Participant.class.getName())) {
            manager = new ParticipantManager();
        }
        // ParticipantStat
        else if (entity.getName().equals(ParticipantStat.class.getName())) {
            manager = new ParticipantStatManager();
        }
        // PlayerStat
        else if (entity.getName().equals(PlayerStat.class.getName())) {
            manager = new PlayerStatManager();
        }
        // Statistic
        else if (entity.getName().equals(AggregatedStatistics.class.getName())) {
            manager = new StatisticManager();
        }*/
        // Core Player
        else if (entity.getName().equals(Player.class.getName())) {
            return (M) new PackagePlayerManager(context);
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