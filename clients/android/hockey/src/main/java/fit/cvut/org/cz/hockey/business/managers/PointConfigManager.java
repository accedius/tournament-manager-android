package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.business.interfaces.IPointConfigManager;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class PointConfigManager implements IPointConfigManager {
    @Override
    public void update(Context context, PointConfiguration pointConfiguration, Long tournamentId) {
        DPointConfiguration dp = PointConfiguration.convertToDPointConfiguration( pointConfiguration );
        DAOFactory.getInstance().pointConfigDAO.update( context, dp, tournamentId);
    }

    @Override
    public PointConfiguration getByTournamentId(Context context, Long tournamentId) {
        PointConfiguration configuration = new PointConfiguration( DAOFactory.getInstance().pointConfigDAO.getByTournamentId( context, tournamentId ));

        return configuration;
    }
}
