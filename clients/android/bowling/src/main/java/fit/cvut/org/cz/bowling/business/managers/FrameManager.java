package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class FrameManager extends BaseManager<Frame> implements IFrameManager {
    @Override
    protected Class<Frame> getMyClass() {
        return Frame.class;
    }

    @Override
    public List<Frame> getByMatchId(long matchId) {
        try {
            IEntityDAO<Frame, Long> frameDAO = managerFactory.getDaoFactory().getMyDao(Frame.class);
            QueryBuilder<Frame, Long> queryBuilder = frameDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cMATCH_ID, matchId);
            queryBuilder.orderBy(DBConstants.cFRAME_NUMBER, true);
            queryBuilder.orderBy(DBConstants.cPARTICIPANT_ID, true);
            PreparedQuery<Frame> preparedQuery = queryBuilder.prepare();
            List<Frame> frames = frameDAO.query(preparedQuery);
            return new ArrayList<>(frames);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Frame> getInMatchByParticipantId(long participantId) {
        try {
            IEntityDAO<Frame, Long> frameDAO = managerFactory.getDaoFactory().getMyDao(Frame.class);
            QueryBuilder<Frame, Long> queryBuilder = frameDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participantId);
            queryBuilder.orderBy(DBConstants.cFRAME_NUMBER, true);
            PreparedQuery<Frame> preparedQuery = queryBuilder.prepare();
            List<Frame> frames = frameDAO.query(preparedQuery);

            for(Frame frame : frames) {
                IRollManager rollManager = ManagerFactory.getInstance().getEntityManager(Roll.class);
                List<Roll> rolls = rollManager.getByFrameId(frame.getId());
                frame.setRolls(rolls);
            }

            return new ArrayList<>(frames);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Frame> getInMatchByPlayerId(long matchId, long playerId) {
        try {
            IEntityDAO<Frame, Long> frameDAO = managerFactory.getDaoFactory().getMyDao(Frame.class);
            QueryBuilder<Frame, Long> queryBuilder = frameDAO.queryBuilder();
            queryBuilder.where().eq(DBConstants.cMATCH_ID, matchId).and().eq(DBConstants.cPLAYER_ID, playerId);
            queryBuilder.orderBy(DBConstants.cFRAME_NUMBER, true);
            PreparedQuery<Frame> preparedQuery = queryBuilder.prepare();
            List<Frame> frames = frameDAO.query(preparedQuery);
            return new ArrayList<>(frames);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteAllByMatchId(long matchId) {
        try {
            IEntityDAO<Frame, Long> frameDAO = managerFactory.getDaoFactory().getMyDao(Frame.class);
            IEntityDAO<Roll, Long> rollDAO = managerFactory.getDaoFactory().getMyDao(Roll.class);
            DeleteBuilder<Frame, Long> frameDeleteBuilder = frameDAO.deleteBuilder();
            frameDeleteBuilder.where().eq(DBConstants.cMATCH_ID, matchId);

            List<Frame> matchFrames = getByMatchId(matchId);
            List<Long> matchFramesId = new ArrayList<>();
            DeleteBuilder<Roll, Long> rollDeleteBuilder = rollDAO.deleteBuilder();
            for (Frame frame : matchFrames) {
                long id = frame.getId();
                matchFramesId.add(id);
            }
            rollDeleteBuilder.where().in(DBConstants.cFRAME_ID, matchFramesId);
            rollDeleteBuilder.delete();
            frameDeleteBuilder.delete();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
