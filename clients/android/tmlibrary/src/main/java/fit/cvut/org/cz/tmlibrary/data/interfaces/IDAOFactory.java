package fit.cvut.org.cz.tmlibrary.data.interfaces;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 14.12.2016.
 */

public interface IDAOFactory {
    <D extends Dao<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz);
}
