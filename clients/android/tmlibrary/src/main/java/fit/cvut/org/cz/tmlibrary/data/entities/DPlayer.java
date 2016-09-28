package fit.cvut.org.cz.tmlibrary.data.entities;

import java.util.Date;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class DPlayer extends DShareBase {
    private long id;
    private String name;
    private String email;
    private String note;

    public DPlayer(long id, String name, String email, String note, String etag, String uid, Date lastModified, Date lastSynchronized) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.note = note;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.lastSynchronized = lastSynchronized;
    }

    public DPlayer(long id, String name, String email, String note) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
