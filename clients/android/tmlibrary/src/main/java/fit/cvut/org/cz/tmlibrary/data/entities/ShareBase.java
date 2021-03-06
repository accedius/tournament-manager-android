package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;
import java.util.UUID;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerToken;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerTokenType;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ISharedEntity;

/**
 * Base for all shared entities.
 */
public abstract class ShareBase implements ISharedEntity, Parcelable {
    protected ShareBase() {
        if (uid == null || uid.isEmpty()) {
            uid = UUID.randomUUID().toString();
        }
    }

    @DatabaseField(columnName = DBConstants.cUID)
    protected String uid;

    @DatabaseField(columnName = DBConstants.cETAG)
    protected String etag;

    @DatabaseField(columnName = DBConstants.cLASTMODIFIED)
    protected Date lastModified;

    @DatabaseField(columnName = DBConstants.cLASTSYNCHRONIZED)
    protected Date lastSynchronized;

    @DatabaseField(columnName = DBConstants.cTOKEN_VALUE)
    protected String tokenValue;

    @DatabaseField(columnName = DBConstants.cTOKEN_TYPE)
    protected ServerTokenType tokenType;

    public Date getLastSynchronized() {
        return lastSynchronized;
    }

    public void setLastSynchronized(Date lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public ServerTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(ServerTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public ServerToken getServerToken() {
        return new ServerToken(getTokenType(), getTokenValue());
    }

    abstract public String getEntityType();
}
