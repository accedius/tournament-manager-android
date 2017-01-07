package fit.cvut.org.cz.tmlibrary.business.serialization.strategies;

import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Serializing strategy used for export.
 */
public class FileSerializingStrategy implements ISerializingStrategy {
    @Override
    public String getUid(ISharedEntity entity) {
        return Long.toString(entity.getId());
    }
}
