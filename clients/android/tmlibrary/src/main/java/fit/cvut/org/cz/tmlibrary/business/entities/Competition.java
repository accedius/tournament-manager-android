package fit.cvut.org.cz.tmlibrary.business.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class Competition extends ShareBase implements Parcelable {

    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String note;
    private CompetitionType type;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static DCompetition convertToDCompetition(Competition c){

        return new DCompetition(c.getId(), c.getName(), c.getStartDate(),
                c.getEndDate(), c.getNote(), c.getType().toString(), c.getEtag(), c.getUid(), c.getLastModified());
    }



    public Competition(long id, String uid, String name, Date startDate, Date endDate, String note, CompetitionType type) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
    }

    public Competition(long id, String name, Date startDate, Date endDate, String note, CompetitionType type) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
    }

    public Competition(DCompetition c) {
        this.id = c.getId();
        this.name = c.getName();
        this.startDate = c.getStartDate();
        this.endDate = c.getEndDate();
        this.note = c.getNote();
        this.type = CompetitionType.valueOf(c.getType());

        this.uid = c.getUid();
        this.etag = c.getEtag();
        this.lastModified = c.getLastModified();
    }

    public Competition(Cursor cursor)  {
        this.id = cursor.getLong(cursor.getColumnIndex(DBConstants.cID));
        this.uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        this.name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse("2015-02-02");
            //endDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cEND)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        //this.type = CompetitionType.valueOf(cursor.getString(cursor.getColumnIndex(DBConstants.cTYPE)));
    }

    protected Competition(Parcel in) {
        id = in.readLong();
        name = in.readString();
        note = in.readString();
        type = CompetitionType.valueOf(in.readString());

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
        dest.writeString(type.toString());
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


    public static final Creator<Competition> CREATOR = new Creator<Competition>() {
        @Override
        public Competition createFromParcel(Parcel in) {
            return new Competition(in);
        }

        @Override
        public Competition[] newArray(int size) {
            return new Competition[size];
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

    public CompetitionType getType() {
        return type;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
