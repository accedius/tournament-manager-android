package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
public class MatchParticipant implements IEntity, Parcelable {
    private long id;
    private String name;

    public long getParticipantId() { return id; }
    public void setParticipantId(long participantId) { this.id = participantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public static final Creator<MatchParticipant> CREATOR = new Creator<MatchParticipant>() {
        @Override
        public MatchParticipant createFromParcel(Parcel in) {
            return new MatchParticipant(in);
        }

        @Override
        public MatchParticipant[] newArray(int size) {
            return new MatchParticipant[size];
        }
    };

    public MatchParticipant(long id, String n) {
        this.id = id;
        this.name = n;
    }

    protected MatchParticipant(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
