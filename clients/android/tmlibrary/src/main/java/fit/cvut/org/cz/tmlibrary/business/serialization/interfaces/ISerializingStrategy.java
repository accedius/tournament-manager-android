package fit.cvut.org.cz.tmlibrary.business.serialization.interfaces;

import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Created by kevin on 21.10.2016.
 */
public interface ISerializingStrategy {
    String getUid(ISharedEntity entity);
}
