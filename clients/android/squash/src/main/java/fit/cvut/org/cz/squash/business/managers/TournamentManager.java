package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.squash.business.entities.PointConfiguration;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    protected SquashDBHelper sportDBHelper;

    public TournamentManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Tournament, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getTournamentDAO();
    }

    @Override
    public void insert(Tournament tournament) {
        try {
            super.insert(tournament);
            PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();
            pointConfiguration.setTournamentId(tournament.getId());
            sportDBHelper.getSquashPointConfigurationDAO().create(pointConfiguration);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
