package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 2.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYER_STATS)
public class PlayerStat implements IEntity, Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    protected long id;

    @DatabaseField(columnName = DBConstants.cPARTICIPANT_ID)
    protected long participant_id;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    protected long player_id;

    private String name;

    public PlayerStat() {}

    public PlayerStat(long participant_id, long player_id) {
        this.participant_id = participant_id;
        this.player_id = player_id;
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

    protected PlayerStat(Parcel in) {
        id = in.readLong();
        participant_id = in.readLong();
        player_id = in.readLong();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(participant_id);
        dest.writeLong(player_id);
        dest.writeString(name);
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

    public long getPlayerId() {
        return player_id;
    }

    public void setPlayerId(long player_id) {
        this.player_id = player_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
