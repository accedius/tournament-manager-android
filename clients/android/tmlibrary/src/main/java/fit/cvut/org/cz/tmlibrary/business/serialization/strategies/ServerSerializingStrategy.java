package fit.cvut.org.cz.tmlibrary.business.serialization.strategies;

import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Created by kevin on 21.10.2016.
 */
public class ServerSerializingStrategy implements ISerializingStrategy {
    @Override
    public String getUid(ISharedEntity entity) {
        return entity.uid;
    }
}
