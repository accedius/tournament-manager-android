package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

@DatabaseTable(tableName = DBConstants.tMATCH_FRAMES)
public class MatchFrame implements Parcelable {
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

    public MatchFrame() {
        //empty
    }

    public MatchFrame(long matchId, long participantId, byte frameNumber, long playerId, List<Integer> rolls) {
        this.matchId = matchId;
        this.participantId = participantId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.rolls = rolls;
    }

    public MatchFrame(long id, long matchId, long participantId, byte frameNumber, long playerId, List<Integer> rolls) {
        this.id = id;
        this.matchId = matchId;
        this.participantId = participantId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.rolls = rolls;
    }

    public MatchFrame(Parcel in) {
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

    public static final Creator<MatchFrame> CREATOR = new Creator<MatchFrame>() {
        @Override
        public MatchFrame createFromParcel(Parcel in) {
            return new MatchFrame(in);
        }

        @Override
        public MatchFrame[] newArray(int size) {
            return new MatchFrame[size];
        }
    };

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
