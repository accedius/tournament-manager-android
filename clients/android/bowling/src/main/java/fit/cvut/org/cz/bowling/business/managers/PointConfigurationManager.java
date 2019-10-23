package fit.cvut.org.cz.bowling.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;

public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    @Override
    protected Class<PointConfiguration> getMyClass() {
        return PointConfiguration.class;
    }

    @Override
    public List<PointConfiguration> getByTournamentId(long tournamentId) {
        try {
            List<PointConfiguration> configurations = managerFactory.getDaoFactory()
                    .getMyDao(PointConfiguration.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            return new ArrayList<>(configurations);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean delete(long id) {
        return super.delete(id);
    }
}
