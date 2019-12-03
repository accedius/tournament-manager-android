package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

@DatabaseTable(tableName = DBConstants.tMATCH_FRAMES)
public class Frame implements Parcelable, IEntity {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cMATCH_ID)
    private long matchId;

    @DatabaseField(columnName = DBConstants.cPARTICIPANT_ID)
    private long participantId;

    @DatabaseField(columnName = DBConstants.cFRAME_NUMBER)
    private byte frameNumber;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    private List<Integer> rolls = null;

    public Frame() {
        //empty, for database needs
    }

    public Frame(long matchId, long participantId, byte frameNumber, long playerId, List<Integer> rolls) {
        this.matchId = matchId;
        this.participantId = participantId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.rolls = rolls;
    }

    public Frame(long id, long matchId, long participantId, byte frameNumber, long playerId, List<Integer> rolls) {
        this.id = id;
        this.matchId = matchId;
        this.participantId = participantId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.rolls = rolls;
    }

    public Frame(Parcel in) {
        id = in.readLong();
        matchId = in.readLong();
        participantId = in.readLong();
        frameNumber = in.readByte();
        playerId = in.readLong();
        in.readList(rolls, Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(matchId);
        dest.writeLong(participantId);
        dest.writeByte(frameNumber);
        dest.writeLong(playerId);
        dest.writeList(rolls);
    }

    public static final Creator<Frame> CREATOR = new Creator<Frame>() {
        @Override
        public Frame createFromParcel(Parcel in) {
            return new Frame(in);
        }

        @Override
        public Frame[] newArray(int size) {
            return new Frame[size];
        }
    };

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Integer> getRolls () {
        return rolls;
    }

    public void setRolls(List<Integer> rolls) {
        this.rolls = rolls;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public byte getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(byte frameNumber) {
        this.frameNumber = frameNumber;
    }
}
