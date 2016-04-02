package fit.cvut.org.cz.tmlibrary.data.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.entities.ShareBase;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class DCompetition extends DShareBase implements Parcelable {

    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;
    private String type;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DCompetition(long id, String name, Date startDate, Date endDate, String note, String type, String etag, String uid, Date lastModified) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
    }

    public DCompetition(long id, String name, Date startDate, Date endDate, String note, String type) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
    }

    public DCompetition(Cursor cursor)  {
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
        this.type = cursor.getString(6);
    }

    protected DCompetition(Parcel in) {
        id = in.readLong();
        name = in.readString();
        note = in.readString();
        type = in.readString();

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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeString(type);
        if (startDate == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(startDate));
        if (endDate == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(endDate));

        //shared base parcelable
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastModified));
        dest.writeString(uid);
        dest.writeString(etag);
    }

    public static final Creator<DCompetition> CREATOR = new Creator<DCompetition>() {
        @Override
        public DCompetition createFromParcel(Parcel in) {
            return new DCompetition(in);
        }

        @Override
        public DCompetition[] newArray(int size) {
            return new DCompetition[size];
        }
    };

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
