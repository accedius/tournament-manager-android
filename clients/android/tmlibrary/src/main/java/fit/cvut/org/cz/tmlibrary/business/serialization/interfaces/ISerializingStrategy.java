package fit.cvut.org.cz.tmlibrary.business.serialization.interfaces;

import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Interface for Serializing strategy.
 */
public interface ISerializingStrategy {
    /**
     * Get entity uid.
     * @param entity entity which id we want to get
     * @return uid
     */
    String getUid(ISharedEntity entity);
}
