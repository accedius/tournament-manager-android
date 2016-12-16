package fit.cvut.org.cz.squash.business;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.business.managers.MatchManager;
import fit.cvut.org.cz.squash.business.managers.TournamentManager;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.CorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by Vaclav on 30. 3. 2016.
 */
public class ManagerFactory extends fit.cvut.org.cz.tmlibrary.business.ManagerFactory {
    private static Context context;
    private static ManagerFactory instance;
    private static Map<String, IDAOFactory> helpersMap = new HashMap<>();

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

    private ManagerFactory() {}

    @Override
    public IDAOFactory getDaoFactory() {
        String name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        if (!helpersMap.containsKey(name))
            helpersMap.put(name, new SquashDBHelper(context, name));
        return helpersMap.get(name);
    }

    public static fit.cvut.org.cz.tmlibrary.business.ManagerFactory getInstance(Context c) {
        context = c;
        if (instance == null)
            instance = new ManagerFactory();
        return instance;
    }

    public static fit.cvut.org.cz.tmlibrary.business.ManagerFactory getInstance() {
        if (instance == null)
            instance = new ManagerFactory();
        return instance;
    }
}