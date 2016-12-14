package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    protected HockeyDBHelper sportDBHelper;

    public TournamentManager(Context context, ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
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
            PointConfiguration pointConfiguration = (PointConfiguration) tournament.getPointConfiguration();
            if (tournament.getPointConfiguration() == null) {
                pointConfiguration = PointConfiguration.defaultConfig();
            }
            pointConfiguration.setTournamentId(tournament.getId());
            sportDBHelper.getHockeyPointConfigurationDAO().create(pointConfiguration);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
