package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISharedEntitySerializer;
import fit.cvut.org.cz.tmlibrary.data.entities.ShareBase;

/**
 * Generic Base Serializer.
 */
abstract public class BaseSerializer<T extends ShareBase> implements ISharedEntitySerializer<T> {
    /**
     * Application context.
     */
    protected static Context context = null;
    /**
     * Serializing strategy.
     */
    protected static ISerializingStrategy strategy = null;

    @Override
    public ServerCommunicationItem serializeToMinimal(T entity) {
        ServerCommunicationItem item = new ServerCommunicationItem();
        item.setUid(strategy.getUid(entity));
        item.setEtag(entity.getEtag());
        item.setToken(entity.getServerToken());
        item.setType(entity.getEntityType());
        return item;
    }
}
