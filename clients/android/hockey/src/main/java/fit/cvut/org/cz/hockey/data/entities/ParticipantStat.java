package fit.cvut.org.cz.hockey.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.hockey.data.helpers.HockeyDBConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Created by kevin on 30.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPARTICIPANT_STATS)
public class ParticipantStat extends fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat implements Parcelable {

    @DatabaseField(columnName = HockeyDBConstants.cSCORE)
    private int score;

    public ParticipantStat() {}

    public ParticipantStat(long participantId, int score) {
        super(participantId);
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(score);
    }

    public ParticipantStat(Parcel in) {
        super(in);
        this.score = in.readInt();
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
}
