package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

@DatabaseTable(tableName = DBConstants.tMATCH_FRAMES)
public class MatchFrame implements Parcelable {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cMATCH_ID)
    private long matchId;

    @DatabaseField(columnName = DBConstants.cFRAME_NUMBER)
    private long frameNumber;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    //gson of the frame rolls
    @DatabaseField(columnName = DBConstants.cFRAME)
    private String frame;

    private List<Integer> rolls = null;

    public MatchFrame() {
        //empty
    }

    public MatchFrame(long matchId, long frameNumber, long playerId, String frame, List<Integer> rolls) {
        this.matchId = matchId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.frame = frame;
        this.rolls = rolls;
    }

    public MatchFrame(long id, long matchId, long frameNumber, long playerId, String frame, List<Integer> rolls) {
        this.id = id;
        this.matchId = matchId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        this.frame = frame;
        this.rolls = rolls;
    }

    public MatchFrame(long matchId, long playerId, long frameNumber, List<Integer> rolls) {
        this.matchId = matchId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        setRolls(rolls);
    }

    public MatchFrame(long id, long matchId, long playerId, long frameNumber, List<Integer> rolls) {
        this.id = id;
        this.matchId = matchId;
        this.frameNumber = frameNumber;
        this.playerId = playerId;
        setRolls(rolls);
    }

    public MatchFrame(Parcel in) {
        id = in.readLong();
        matchId = in.readLong();
        frameNumber = in.readLong();
        playerId = in.readLong();
        frame = in.readString();
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
        dest.writeLong(frameNumber);
        dest.writeLong(playerId);
        dest.writeString(frame);
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

    private void updateRolls () {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        rolls = gson.fromJson(frame, type);
    }

    public List<Integer> getRolls () {
        if(rolls == null || rolls.isEmpty()) {
            updateRolls();
        }
        return rolls;
    }

    public void setRolls(List<Integer> rolls) {
        this.rolls = rolls;
        Gson gson = new Gson();
        this.frame = gson.toJson(rolls);
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

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
}
