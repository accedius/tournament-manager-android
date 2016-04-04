package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class Player extends ShareBase implements Parcelable {

    private long id;
    private String name;
    private String email;
    private String note;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Creator<Player> getCREATOR() {
        return CREATOR;
    }

    protected Player(Parcel in) {
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

    public Player(DPlayer p) {
        this.id = p.getId();
        this.name = p.getName();
        this.email = p.getEmail();
        this.note = p.getNote();

        this.uid = p.getUid();
        this.etag = p.getEtag();
        this.lastModified = p.getLastModified();
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
        dest.writeString(uid);
        dest.writeString(etag);
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
}
