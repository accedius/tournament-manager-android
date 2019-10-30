package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class PointConfigurationManager extends BaseManager<PointConfiguration> implements IPointConfigurationManager {
    @Override
    protected Class<PointConfiguration> getMyClass() {
        return PointConfiguration.class;
    }

    @Override
    public List<PointConfiguration> getByTournamentId(long tournamentId) {
        try {
            IEntityDAO<PointConfiguration, Long> pointConfigurationDAO = managerFactory.getDaoFactory().getMyDao(PointConfiguration.class);
            QueryBuilder<PointConfiguration, Long> queryBuilder = pointConfigurationDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cTOURNAMENT_ID, tournamentId);
            queryBuilder.orderBy(DBConstants.cSIDES_NUMBER, true);
            PreparedQuery<PointConfiguration> preparedQuery = queryBuilder.prepare();
            List<PointConfiguration> configurations = pointConfigurationDAO.query(preparedQuery);
            return new ArrayList<>(configurations);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public PointConfiguration getBySidesNumber (long tournamentId, long sidesNumber) {
        try {
            IEntityDAO<PointConfiguration, Long> pointConfigurationDAO = managerFactory.getDaoFactory().getMyDao(PointConfiguration.class);
            QueryBuilder<PointConfiguration, Long> queryBuilder = pointConfigurationDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cTOURNAMENT_ID, tournamentId).and().eq(DBConstants.cSIDES_NUMBER, sidesNumber);
            PreparedQuery<PointConfiguration> preparedQuery = queryBuilder.prepare();
            PointConfiguration pointConfiguration = pointConfigurationDAO.queryForFirst(preparedQuery);
            return pointConfiguration;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean delete(long id) {
        return super.delete(id);
    }
}
