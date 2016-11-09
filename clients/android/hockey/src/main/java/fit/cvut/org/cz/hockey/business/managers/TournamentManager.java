package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    @Override
    protected Dao<Tournament, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getTournamentDao();
    }

    @Override
    public void insert(Context context, Tournament tournament) {
        super.insert(context, tournament);
        PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();
        pointConfiguration.setTournamentId(tournament.getId());
        ManagerFactory.getInstance().pointConfigManager.insert(context, pointConfiguration);
    }

    @Override
    public boolean delete(Context context, long id) {
        // TODO check if has matches or players or teams
        ManagerFactory.getInstance().pointConfigManager.delete(context, id);
        return super.delete(context, id);
    }
}
