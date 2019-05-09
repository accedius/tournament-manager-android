package fit.cvut.org.cz.tmlibrary.data.entities;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class EntityDAO<E extends IEntity, ID extends Long> extends RuntimeExceptionDao<E, ID> implements IEntityDAO<E, ID> {
    private static Logger logger;
    protected Dao<E, ID> EntityDAO;

    public EntityDAO (Dao<E, ID> dao) {
        super(dao);
        this.EntityDAO = dao;
        logger = LoggerFactory.getLogger(EntityDAO.class);
    }

    public List<E> getListItemById (String DBConstant, Object id) {
        try {
            return EntityDAO.queryForEq(DBConstant, id);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on getListItemById", e);
            return new ArrayList<>();
        }
    }

    public int deleteItemById (String DBConstant, Object id) throws SQLException {
        try {
            DeleteBuilder<E, ID> builder = EntityDAO.deleteBuilder();
            builder.where().eq(DBConstant, id);
            return builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on deleteItemById", e);
            throw e;
        }
    }

    public int updateItem (E itemToUpdate) {
        try {
            return EntityDAO.update(itemToUpdate);
        } catch(SQLException e) {
            e.printStackTrace();
            logger.error("Sql Exception: on updateItem", e);
            return -1;
        }
    }
}
