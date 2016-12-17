package fit.cvut.org.cz.hockey.business.managers;

import java.sql.SQLException;

import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    @Override
    public void insert(Tournament tournament) {
        try {
            super.insert(tournament);
            PointConfiguration pointConfiguration = (PointConfiguration) tournament.getPointConfiguration();
            if (tournament.getPointConfiguration() == null) {
                pointConfiguration = PointConfiguration.defaultConfig();
            }
            pointConfiguration.setTournamentId(tournament.getId());
            managerFactory.getDaoFactory().getMyDao(PointConfiguration.class).create(pointConfiguration);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
