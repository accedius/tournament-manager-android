package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;
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
    public List<Roll> getByFrameId(long frameId) {
        try {
            IEntityDAO<Roll, Long> rollDAO = managerFactory.getDaoFactory().getMyDao(Roll.class);
            QueryBuilder<Roll, Long> queryBuilder = rollDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cFRAME_ID, frameId);
            queryBuilder.orderBy(DBConstants.cROLL_NUMBER, true);
            PreparedQuery<Roll> preparedQuery = queryBuilder.prepare();
            List<Roll> rolls = rollDAO.query(preparedQuery);
            return new ArrayList<>(rolls);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteByFrameId(long frameId) {
        try {
            IEntityDAO<Roll, Long> rollDAO = managerFactory.getDaoFactory().getMyDao(Roll.class);
            DeleteBuilder<Roll, Long> rollDeleteBuilder = rollDAO.deleteBuilder();
            rollDeleteBuilder.where().eq(DBConstants.cFRAME_ID, frameId);
            rollDeleteBuilder.delete();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
