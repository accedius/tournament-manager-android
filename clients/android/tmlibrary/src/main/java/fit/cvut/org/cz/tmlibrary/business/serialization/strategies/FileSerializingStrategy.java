package fit.cvut.org.cz.tmlibrary.business.serialization.strategies;

import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;

/**
 * Created by kevin on 21.10.2016.
 */
public class FileSerializingStrategy implements ISerializingStrategy {
    @Override
    public String getUid(ISharedEntity entity) {
        return Long.toString(entity.getId());
    }
}
