package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
public class Participant implements Parcelable
{
    private long participantId;
    private String name;

    public long getParticipantId() { return participantId; }
    public void setParticipantId( long participantId ) { this.participantId = participantId; }
    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    public Participant( long id, String n )
    {
        this.participantId = id;
        this.name = n;
    }

    protected Participant( Parcel in )
    {
        this.participantId = in.readLong();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong( participantId );
        dest.writeString( name );
    }

    @Override
    public String toString() {
        return name;
    }


}
