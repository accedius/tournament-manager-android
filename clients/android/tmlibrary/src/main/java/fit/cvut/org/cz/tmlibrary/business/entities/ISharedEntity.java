package fit.cvut.org.cz.tmlibrary.business.entities;

import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.serialization.ServerTokenType;

/**
 * Created by kevin on 7.10.2016.
 */
public interface ISharedEntity extends IEntity {
    String uid = null;
    String etag = null;
    String tokenValue = null;
    ServerTokenType tokenType = null;
    Date lastModified = null;
    Date lastSynchronized = null;
}
