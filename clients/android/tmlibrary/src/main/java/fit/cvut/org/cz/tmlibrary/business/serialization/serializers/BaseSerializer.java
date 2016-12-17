package fit.cvut.org.cz.tmlibrary.business.serialization.serializers;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISharedEntitySerializer;
import fit.cvut.org.cz.tmlibrary.data.entities.ShareBase;

/**
 * Created by kevin on 10.10.2016.
 */
abstract public class BaseSerializer<T extends ShareBase> implements ISharedEntitySerializer<T> {
    protected static Context context = null;
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
