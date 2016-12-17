package fit.cvut.org.cz.tmlibrary.business.serialization.interfaces;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Created by kevin on 7.10.2016.
 */
public interface ISharedEntitySerializer<T extends ISharedEntity> {
    ServerCommunicationItem serialize (T entity);
    ServerCommunicationItem serializeToMinimal (T entity);
    T deserialize(ServerCommunicationItem item);
    HashMap<String, Object> serializeSyncData(T entity);
    void deserializeSyncData(HashMap<String, Object> syncData, T entity);
    String getEntityType();
}
