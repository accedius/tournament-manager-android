package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    protected HockeyDBHelper sportDBHelper;

    public TournamentManager(ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Tournament, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getTournamentDAO();
    }

    @Override
    public void insert(Context context, Tournament tournament) {
        try {
            super.insert(context, tournament);
            PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();
            pointConfiguration.setTournamentId(tournament.getId());
            sportDBHelper.getHockeyPointConfigurationDAO().create(pointConfiguration);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
