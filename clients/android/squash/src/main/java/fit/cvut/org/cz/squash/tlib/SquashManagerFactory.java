package fit.cvut.org.cz.squash.tlib;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.tlib.CorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.tlib.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.tlib.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 14.12.2016.
 */

public class SquashManagerFactory extends ManagerFactory {
    private static Context context;
    private static SquashManagerFactory instance;
    private static SquashHelper2 daoFactory;

    @Override
    public <M extends IManager<E>, E extends IEntity> M getEntityManager(Class<E> entity) {
        if (entity.getName().equals(Competition.class.getName())) {
            return (M) new CompetitionManager();
        } else if (entity.getName().equals(Tournament.class.getName())) {
            return (M) new TournamentManager();
        } else if (entity.getName().equals(Match.class.getName())) {
            return (M) new MatchManager();
        } else if (entity.getName().equals(Player.class.getName())) {
            return (M) new CorePlayerManager(context);
        }
        return null;
    }

    private SquashManagerFactory() {}

    // TODO nezapomenout udělat mapu pro všechny kontexty
    @Override
    public IDAOFactory getDaoFactory() {
        String name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        // if map.contains(name) ...
        if (daoFactory == null) {
            daoFactory = new SquashHelper2(context, name);
        }
        return daoFactory;
    }

    public static ManagerFactory getInstance(Context c) {
        context = c;
        if (instance == null)
            instance = new SquashManagerFactory();
        return instance;
    }

    public static ManagerFactory getInstance() {
        if (instance == null)
            instance = new SquashManagerFactory();
        return instance;
    }
}