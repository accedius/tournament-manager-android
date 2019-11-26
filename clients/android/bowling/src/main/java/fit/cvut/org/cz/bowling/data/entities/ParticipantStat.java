package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import fit.cvut.org.cz.bowling.business.entities.MatchFrame;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

/**
 * ParticipantStat entity (stats of a match/tournament participant, could be a team or individual player - based on competition or tournament type, so basically it is a unit/atom that match operates with) and its representation in database
 */
@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tPARTICIPANT_STATS)
public class ParticipantStat extends fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat implements Parcelable {

    @DatabaseField(columnName = DBConstants.cSCORE)
    private int score;

    @DatabaseField(columnName = DBConstants.cFRAMES_NUMBER)
    private int framesPlayedNumber;

    private List<MatchFrame> frames;

    public ParticipantStat() {}

    public ParticipantStat(long participantId, int score, int framesPlayedNumber) {
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
        dest.writeInt(framesPlayedNumber);
    }

    public ParticipantStat(Parcel in) {
        super(in);
        this.score = in.readInt();
        this.framesPlayedNumber = in.readInt();
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

    public int getFramesPlayedNumber() {
        return framesPlayedNumber;
    }

    public void setFramesPlayedNumber(int framesPlayedNumber) {
        this.framesPlayedNumber = framesPlayedNumber;
    }
}
