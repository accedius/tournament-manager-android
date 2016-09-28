package fit.cvut.org.cz.tmlibrary.data.entities;

import java.util.Date;

/**
 * Created by atgot_000 on 3. 4. 2016.
 */
public class DTournament extends DShareBase {
    private long id;
    private long competitionId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;

    public DTournament(long id, String name, Date startDate, Date endDate, String note, String etag, String uid, Date lastModified, Date lastSynchronized, long competitionId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.lastSynchronized = lastSynchronized;
        this.competitionId  = competitionId;
    }

    public DTournament(long id, String name, Date startDate, Date endDate, String note) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(long competitionId) {
        this.competitionId = competitionId;
    }
}
