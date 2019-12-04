package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class RollManager extends BaseManager<Roll> implements IRollManager {
    @Override
    protected Class<Roll> getMyClass() {
        return Roll.class;
    }

    @Override
    public List<Roll> getByFrameId(long matchId, long frameId) {
        try {
            IEntityDAO<Roll, Long> rollDAO = managerFactory.getDaoFactory().getMyDao(Roll.class);
            QueryBuilder<Roll, Long> queryBuilder = rollDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cMATCH_ID, matchId).and().eq(DBConstants.cFRAME_ID, frameId);
            queryBuilder.orderBy(DBConstants.cROLL_NUMBER, true);
            PreparedQuery<Roll> preparedQuery = queryBuilder.prepare();
            List<Roll> configurations = rollDAO.query(preparedQuery);
            return new ArrayList<>(configurations);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
