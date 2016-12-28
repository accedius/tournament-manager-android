package fit.cvut.org.cz.tmlibrary.data.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
@DatabaseTable (tableName = DBConstants.tPLAYERS)
public class Player extends ShareBase implements Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private Long id;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cNAME)
    private String name;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cEMAIL)
    private String email;

    @DatabaseField(columnName = DBConstants.cNOTE)
    private String note;

    public static final String col_name = "name";
    public static final String col_email = "email";

    private static SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

    public static Creator<Player> getCREATOR() {
        return CREATOR;
    }

    public Player() {}

    protected Player(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        note = in.readString();

        try {
            String text = in.readString();
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

    public Player(long id, String name, String email, String note) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.note = note;
    }

    public Player(long id, String uid, String name, String email, String note) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.note = note;
    }

    public Player(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(DBConstants.cID));
        this.uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        this.name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));
        this.email = cursor.getString(cursor.getColumnIndex(DBConstants.cEMAIL));
        this.note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("email", getEmail());
        values.put("name", getName());
        values.put("note", getNote());
        return values;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(note);

        //shared base parcelable
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastModified));
        if (lastSynchronized == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastSynchronized));
        dest.writeString(uid);
        dest.writeString(etag);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return id == player.id;
    }

    public boolean samePlayer(Player p) {
        if (this == p) return true;
        return (email.equals(p.email) && note.equals(p.note) && name.equals(p.name));
    }

    public String getColumn(String column) {
        if (column.equals(col_name)) {
            return getName();
        } else if (column.equals(col_email)) {
            return getEmail();
        } else {
            return "";
        }
    }

    public String getEntityType() {
        return "Player";
    }
}
