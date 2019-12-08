package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

/**
 * ParticipantStat entity (stats of a match/tournament participant, could be a team or individual player - based on competition or tournament type, so basically it is a unit/atom that match operates with) and its representation in database
 */
@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tPARTICIPANT_STATS)
public class ParticipantStat extends fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat implements Parcelable {

    @DatabaseField(columnName = DBConstants.cSCORE)
    private int score;

    @DatabaseField(columnName = DBConstants.cFRAMES_NUMBER)
    private byte framesPlayedNumber;

    private List<Frame> frames;

    public ParticipantStat() {}

    public ParticipantStat(long participantId, int score) {
        this.score = score;
        framesPlayedNumber = 0;
    }

    public ParticipantStat(long participantId, int score, byte framesPlayedNumber) {
        super(participantId);
        this.score = score;
        this.framesPlayedNumber = framesPlayedNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(score);
        dest.writeByte(framesPlayedNumber);
    }

    public ParticipantStat(Parcel in) {
        super(in);
        this.score = in.readInt();
        this.framesPlayedNumber = in.readByte();
    }

    public static final Creator<ParticipantStat> CREATOR = new Creator<ParticipantStat>() {
        @Override
        public ParticipantStat createFromParcel(Parcel in) {
            return new ParticipantStat(in);
        }

        @Override
        public ParticipantStat[] newArray(int size) {
            return new ParticipantStat[size];
        }
    };

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

    public List<Frame> getFrames() {
        return frames;
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }
}
