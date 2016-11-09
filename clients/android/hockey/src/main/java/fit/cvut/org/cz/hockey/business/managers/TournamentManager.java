package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
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

    @Override
    protected Dao<Tournament, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getTournamentDao();
    }

    @Override
    public ArrayList<Tournament> getByCompetitionId(Context context, long competitionId) {
        ArrayList<Tournament> res = new ArrayList<>();
        try {
            List<Tournament> tournaments = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cCOMPETITION_ID, competitionId)
                    .query();
            res.addAll(tournaments);
            return res;
        } catch (SQLException e) {
            return res;
        }
    }
}
