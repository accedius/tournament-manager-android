package fit.cvut.org.cz.squash.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.squash.data.helpers.SDBConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Created by kevin on 9.12.2016.
 */

@DatabaseTable(tableName = DBConstants.tPARTICIPANT_STATS)
public class ParticipantStat extends fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat implements Parcelable {

    @DatabaseField(columnName = SDBConstants.cSET_NUMBER)
    private int setNumber;

    @DatabaseField(columnName = SDBConstants.cPOINTS)
    private int points;

    public ParticipantStat() {}

    public ParticipantStat(long participantId, int setNumber, int points) {
        super(participantId);
        this.setNumber = setNumber;
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(setNumber);
        dest.writeInt(points);
    }

    public ParticipantStat(Parcel in) {
        super(in);
        this.setNumber = in.readInt();
        this.points = in.readInt();
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }
}
