package fit.cvut.org.cz.tmlibrary.business.serialization.strategies;

import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Serializing strategy used for server serializing.
 */
public class ServerSerializingStrategy implements ISerializingStrategy {
    @Override
    public String getUid(ISharedEntity entity) {
        return entity.uid;
    }
}
