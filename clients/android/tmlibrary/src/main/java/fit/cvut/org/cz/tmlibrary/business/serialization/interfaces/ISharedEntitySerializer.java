package fit.cvut.org.cz.tmlibrary.business.serialization.interfaces;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Interface for Entity Serializer.
 */
public interface ISharedEntitySerializer<T extends ISharedEntity> {
    /**
     * Serialize entity to ServerCommunicationItem
     * @param entity entity to be serialized
     * @return serialized item
     */
    ServerCommunicationItem serialize (T entity);

    /**
     * Serialize minimal entity information to ServerCommunicationItem
     * @param entity entity to be serialized
     * @return serialized item
     */
    ServerCommunicationItem serializeToMinimal (T entity);

    /**
     * Deserialize Entity from ServerCommunicationItem
     * @param item item to be deserialized
     * @return entity
     */
    T deserialize(ServerCommunicationItem item);

    /**
     * Serialize syncdata of entity.
     * @param entity entity to be serialized
     * @return hashmap with entity attribute name as a key and attribute value as a value
     */
    HashMap<String, Object> serializeSyncData(T entity);

    /**
     * Deserialize syncdata of entity.
     * @param syncData deserialized syncdata
     * @param entity entity to be deserialized
     */
    void deserializeSyncData(HashMap<String, Object> syncData, T entity);

    /**
     * Entity Type getter.
     * @return type
     */
    String getEntityType();
}
