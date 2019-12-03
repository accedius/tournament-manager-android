package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ParticipantOverview implements Parcelable {
    private long participantStatId;
    private String name;
    private int score;
    private byte framesPlayedNumber;

    public ParticipantOverview() {
        //nothing
    }

    public ParticipantOverview(long participantStatId, String name, int score, byte framesPlayedNumber) {
        this.participantStatId = participantStatId;
        this.name = name;
        this.score = score;
        this.framesPlayedNumber = framesPlayedNumber;
    }

    public ParticipantOverview(Parcel in) {
        participantStatId = in.readLong();
        name = in.readString();
        score = in.readInt();
        framesPlayedNumber = in.readByte();
    }

    public static final Creator<ParticipantOverview> CREATOR = new Creator<ParticipantOverview>() {
        @Override
        public ParticipantOverview createFromParcel(Parcel in) {
            return new ParticipantOverview(in);
        }

        @Override
        public ParticipantOverview[] newArray(int size) {
            return new ParticipantOverview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(participantStatId);
        dest.writeString(name);
        dest.writeInt(score);
        dest.writeByte(framesPlayedNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public byte getFramesPlayedNumber() {
        return framesPlayedNumber;
    }

    public void setFramesPlayedNumber(byte framesPlayedNumber) {
        this.framesPlayedNumber = framesPlayedNumber;
    }

    public long getParticipantStatId() {
        return participantStatId;
    }

    public void setParticipantStatId(long participantStatId) {
        this.participantStatId = participantStatId;
    }
}
