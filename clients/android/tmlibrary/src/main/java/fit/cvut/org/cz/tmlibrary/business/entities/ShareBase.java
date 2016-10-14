package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcelable;

import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.serialization.ServerToken;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerTokenType;

/**
 * Created by Vaclav on 2. 4. 2016.
 */
public abstract class ShareBase implements ISharedEntity, Parcelable {
    protected String uid;
    protected String etag;
    protected Date lastModified;
    protected Date lastSynchronized;
    protected String tokenValue;
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
