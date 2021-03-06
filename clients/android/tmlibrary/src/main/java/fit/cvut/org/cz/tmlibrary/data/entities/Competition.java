package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
@DatabaseTable(tableName = DBConstants.tCOMPETITIONS)
public class Competition extends ShareBase implements Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cSPORT_NAME)
    private String sportContext;

    @DatabaseField(columnName = DBConstants.cNAME)
    private String name;

    @DatabaseField(columnName = DBConstants.cSTART)
    private Date startDate;

    @DatabaseField(columnName = DBConstants.cEND)
    private Date endDate;

    @DatabaseField(columnName = DBConstants.cNOTE)
    private String note;

    @DatabaseField(columnName = DBConstants.cTYPE)
    private int typeId;

    private CompetitionType type;

    private static SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

    public Competition() {}

    public Competition(long id, String uid, String name, Date startDate, Date endDate, String note, CompetitionType type) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
        this.typeId = type.id;
    }

    public Competition(long id, String name, Date startDate, Date endDate, String note, CompetitionType type) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.type = type;
        this.typeId = type.id;
    }

    protected Competition(Parcel in) {
        id = in.readLong();
        name = in.readString();
        note = in.readString();
        typeId = in.readInt();
        type = in.readParcelable(CompetitionType.class.getClassLoader());

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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeInt(typeId);
        dest.writeParcelable(type, flags);
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
        if (type != null)
            this.typeId = type.id;
    }

    public String getEntityType() {
        return Constants.COMPETITION;
    }

    public String getSportContext() {
        return sportContext;
    }

    public void setSportContext(String sport_context) {
        this.sportContext = sport_context;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getColumn(String column) {
        if (column.equals(DBConstants.cNAME)) {
            return getName();
        } else if (column.equals(DBConstants.cSTART)) {
            if (startDate == null) return "";

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            return Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString(cal.get(Calendar.MONTH)) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        } else if (column.equals(DBConstants.cEND)) {
            if (endDate == null) return "";

            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            return Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString(cal.get(Calendar.MONTH)) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        } else {
            return "";
        }
    }

    public String getFilename() {
        return (name + "_" + DateFormatter.getInstance().getDBDateTimeFormat().format(new Date())).replace(" ", "_");
    }
}
