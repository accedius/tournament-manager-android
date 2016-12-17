package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 30.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPARTICIPANT_STATS)
public class ParticipantStat implements IEntity, Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cPARTICIPANT_ID)
    // Team id or Player id (based on Competition type)
    private long participant_id;

    public ParticipantStat() {}

    public ParticipantStat(long participant_id) {
        this.participant_id = participant_id;
    }

    public static final Creator<PlayerStat> CREATOR = new Creator<PlayerStat>() {
        @Override
        public PlayerStat createFromParcel(Parcel in) {
            return new PlayerStat(in);
        }

        @Override
        public PlayerStat[] newArray(int size) {
            return new PlayerStat[size];
        }
    };

    protected ParticipantStat(Parcel in) {
        this.id = in.readLong();
        this.participant_id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(participant_id);
    }
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParticipantId() {
        return participant_id;
    }

    public void setParticipantId(long participant_id) {
        this.participant_id = participant_id;
    }
}
