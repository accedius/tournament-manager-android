package fit.cvut.org.cz.tmlibrary.data.entities;

import java.util.Date;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class DCompetition extends DShareBase {
    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;
    private int type;

    public DCompetition(long id, String name, Date startDate, Date endDate, String note, int type, String etag, String uid, Date lastModified, Date lastSynchronized) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.lastSynchronized = lastSynchronized;
    }

    public DCompetition(long id, String name, Date startDate, Date endDate, String note, int type) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
