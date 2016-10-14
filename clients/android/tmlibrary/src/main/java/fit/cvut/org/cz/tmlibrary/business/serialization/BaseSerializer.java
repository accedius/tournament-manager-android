package fit.cvut.org.cz.tmlibrary.business.serialization;

import fit.cvut.org.cz.tmlibrary.business.entities.ShareBase;

/**
 * Created by kevin on 10.10.2016.
 */
abstract public class BaseSerializer<T extends ShareBase> implements ISharedEntitySerializer<T> {
    @Override
    public ServerCommunicationItem serializeToMinimal(T entity) {
        ServerCommunicationItem item = new ServerCommunicationItem();
        item.setUid(entity.getUid());
        item.setEtag(entity.getEtag());
        item.setToken(entity.getServerToken());
        item.setType(entity.getEntityType());
        return item;
    }
}
