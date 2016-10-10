package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class Tournament extends ShareBase implements Parcelable {
    public static final String col_name = "name";
    public static final String col_start_date = "start_date";
    public static final String col_end_date= "end_date";

    private long id;
    private long competitionId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;

    private static SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

    public static DTournament convertToDTournament(Tournament c){
        return new DTournament(c.getId(), c.getName(), c.getStartDate(),
                c.getEndDate(), c.getNote(), c.getEtag(), c.getUid(), c.getLastModified(), c.getLastSynchronized(), c.getCompetitionId());
    }

    public Tournament(long id, String uid, String name, Date startDate, Date endDate, String note) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public Tournament(long id, long competitionId, String name, Date startDate, Date endDate, String note) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.setCompetitionId(competitionId);
    }

    public Tournament(DTournament c) {
        this.id = c.getId();
        this.name = c.getName();
        this.startDate = c.getStartDate();
        this.endDate = c.getEndDate();
        this.note = c.getNote();
        this.competitionId = c.getCompetitionId();

        this.uid = c.getUid();
        this.etag = c.getEtag();
        this.lastModified = c.getLastModified();
        this.lastSynchronized = c.getLastSynchronized();
    }

    protected Tournament(Parcel in) {
        id = in.readLong();
        name = in.readString();
        note = in.readString();

        try {
            String text = in.readString();
            if (text == null) startDate = null;
            else startDate = dateFormat.parse(text);

            text = in.readString();
            if (text == null) endDate = null;
            else endDate = dateFormat.parse(text);

            text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = dateFormat.parse(text);

            text = in.readString();
            if (text == null) lastSynchronized = null;
            else lastSynchronized = dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = in.readString();
        etag = in.readString();
        competitionId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(note);
        if (startDate == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(startDate));
        if (endDate == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(endDate));

        //shared base parcelable
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastModified));
        if (lastSynchronized == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastSynchronized));
        dest.writeString(uid);
        dest.writeString(etag);
        dest.writeLong(competitionId);
    }

    public static final Creator<Tournament> CREATOR = new Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }

        @Override
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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

    public String getColumn(String column) {
        if (column.equals("name")) {
            return name;
        } else if (column.equals("start_date")) {
            return DateFormatter.getInstance().getDBDateFormat().format(startDate);
        } else if (column.equals("end_date")) {
            return DateFormatter.getInstance().getDBDateFormat().format(endDate);
        } else {
            return "";
        }
    }

    public String getEntityType() {
        return "Tournament";
    }
}
