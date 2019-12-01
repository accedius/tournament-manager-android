package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

@DatabaseTable(tableName = DBConstants.tMATCH_FRAME_ROLLS)
public class MatchFrameRoll implements Parcelable {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cFRAME_ID)
    private long frameId;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    @DatabaseField(columnName = DBConstants.cPOINTS)
    private byte points;

    public MatchFrameRoll(long id, long frameId, long playerId, byte points) {
        this.id = id;
        this.frameId = frameId;
        this.playerId = playerId;
        this.points = points;
    }

    public MatchFrameRoll(long frameId, long playerId, byte points) {
        this.frameId = frameId;
        this.playerId = playerId;
        this.points = points;
    }

    public MatchFrameRoll (Parcel in) {
        id = in.readLong();
        frameId = in.readLong();
        playerId = in.readLong();
        points = in.readByte();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(frameId);
        dest.writeLong(playerId);
        dest.writeByte(points);
    }

    public static final Creator<MatchFrameRoll> CREATOR = new Creator<MatchFrameRoll>() {
        @Override
        public MatchFrameRoll createFromParcel(Parcel in) {
            return new MatchFrameRoll(in);
        }

        @Override
        public MatchFrameRoll[] newArray(int size) {
            return new MatchFrameRoll[size];
        }
    };

    public long getFrameId() {
        return frameId;
    }

    public void setFrameId(long frameId) {
        this.frameId = frameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public byte getPoints() {
        return points;
    }

    public void setPoints(byte points) {
        this.points = points;
    }
}
