package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Vaclav on 2. 4. 2016.
 */
public abstract class ShareBase implements Parcelable {

    protected String uid;
    protected String etag;
    protected Date lastModified;


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
}
