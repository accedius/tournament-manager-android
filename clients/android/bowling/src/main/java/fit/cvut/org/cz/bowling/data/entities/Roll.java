package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

@DatabaseTable(tableName = DBConstants.tMATCH_FRAME_ROLLS)
public class Roll implements Parcelable, IEntity {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cFRAME_ID)
    private long frameId;

    @DatabaseField(columnName = DBConstants.cROLL_NUMBER)
    private byte rollNumber;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    @DatabaseField(columnName = DBConstants.cPOINTS)
    private byte points;

    public Roll(long id, long frameId, byte rollNumber, long playerId, byte points) {
        this.id = id;
        this.frameId = frameId;
        this.rollNumber = rollNumber;
        this.playerId = playerId;
        this.points = points;
    }

    public Roll(long frameId, byte rollNumber, long playerId, byte points) {
        this.frameId = frameId;
        this.rollNumber = rollNumber;
        this.playerId = playerId;
        this.points = points;
    }

    public Roll(Parcel in) {
        id = in.readLong();
        frameId = in.readLong();
        rollNumber = in.readByte();
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
        dest.writeByte(rollNumber);
        dest.writeLong(playerId);
        dest.writeByte(points);
    }

    public static final Creator<Roll> CREATOR = new Creator<Roll>() {
        @Override
        public Roll createFromParcel(Parcel in) {
            return new Roll(in);
        }

        @Override
        public Roll[] newArray(int size) {
            return new Roll[size];
        }
    };

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFrameId() {
        return frameId;
    }

    public void setFrameId(long frameId) {
        this.frameId = frameId;
    }

    public byte getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(byte rollNumber) {
        this.rollNumber = rollNumber;
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
