package fit.cvut.org.cz.tmlibrary.data.interfaces;

/**
 * Interface for DAO Factory.
 */
public interface IDAOFactory {
    /**
     * Method for getting DAO by given class.
     * @param clazz class for DAO should be got
     * @param <D> DAO interface
     * @param <E> Entity class
     * @return class' DAO interface
     */
    <D extends IEntityDAO<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz);

    /*<D extends Dao<E, Long>, E extends IEntity> D getMyDao(Class<E> clazz);

    <D extends IEntityDAO<E, Long>, E extends IEntity> D getEntityDAO(Class<E> clazz);*/
}
