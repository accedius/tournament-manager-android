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
public class DPlayer extends DShareBase implements Parcelable {

    private long id;
    private String name;
    private String email;
    private String note;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DPlayer(long id, String name, String email, String note, String etag, String uid, Date lastModified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.note = note;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
    }

    public DPlayer(long id, String name, String email, String note) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.note = note;
    }

    protected DPlayer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        note = in.readString();

        try{
            String text = in.readString();
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
        dest.writeString(email);
        dest.writeString(note);

        //shared base parcelable
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastModified));
        dest.writeString(uid);
        dest.writeString(etag);
    }

    public static final Creator<DPlayer> CREATOR = new Creator<DPlayer>() {
        @Override
        public DPlayer createFromParcel(Parcel in) {
            return new DPlayer(in);
        }

        @Override
        public DPlayer[] newArray(int size) {
            return new DPlayer[size];
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

    @Override
    public int describeContents() {
        return 0;
    }
}
