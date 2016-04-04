package fit.cvut.org.cz.tmlibrary.data.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by atgot_000 on 3. 4. 2016.
 */
public class DTournament extends DShareBase implements Parcelable {

    private long id;
    private long competitionId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final Creator<DTournament> CREATOR = new Creator<DTournament>() {
        @Override
        public DTournament createFromParcel(Parcel in) {
            return new DTournament(in);
        }

        @Override
        public DTournament[] newArray(int size) {
            return new DTournament[size];
        }
    };

    public DTournament(long id, String name, Date startDate, Date endDate, String note, String etag, String uid, Date lastModified, long competitionId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.competitionId  = competitionId;
    }

    public DTournament(long id, String name, Date startDate, Date endDate, String note) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
    }

    public DTournament(Cursor cursor)  {
        this.id = cursor.getInt(0);
        this.uid = cursor.getString(1);
        this.name = cursor.getString(2);

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(cursor.getString(3));
            endDate = dateFormat.parse(cursor.getString(4));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.note = cursor.getString(5);
    }

    protected DTournament(Parcel in) {
        id = in.readLong();
        name = in.readString();
        note = in.readString();

        try{
            String text = in.readString();
            if (text == null) startDate = null;
            else startDate = dateFormat.parse(text);

            text = in.readString();
            if (text == null) endDate = null;
            else endDate = dateFormat.parse(text);

            text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = dateFormat.parse(text);
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
        dest.writeString(uid);
        dest.writeString(etag);
        dest.writeLong(competitionId);
    }

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
}
