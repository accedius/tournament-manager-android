package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.entities.ISharedEntity;

/**
 * Created by kevin on 7.10.2016.
 */
public interface ISharedEntitySerializer<T extends ISharedEntity> {
    ServerCommunicationItem serialize (T entity);
    ServerCommunicationItem serializeToMinimal (T entity);
    T deserialize(ServerCommunicationItem item);
    HashMap<String, String> serializeSyncData(T entity);
    void deserializeSyncData(String syncData, T entity);
    String getEntityType();
}
