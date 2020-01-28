package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FrameOverview implements Parcelable {
    private byte frameNumber;
    private List<Byte> rolls;
    private String playerName;
    private int currentScore;
    private long playerId;
    private long participantId;

    //only frame rolls score, nothing else, no bonuses from strike or spare except 10th frame, where bonuses are interpreted as frame rolls
    private byte frameScore;

    public FrameOverview() {
        // empty
    }

    public FrameOverview(FrameOverview overview) {
        this.frameNumber = overview.getFrameNumber();
        this.rolls = new ArrayList<>(overview.getRolls());
        this.playerName = overview.getPlayerName();
        this.currentScore = overview.getCurrentScore();
        this.playerId = overview.getPlayerId();
        this.participantId = overview.getParticipantId();
        this.frameScore = overview.getFrameScore();
    }

    public FrameOverview(byte frameNumber, List<Byte> rolls, String playerName, Integer currentScore, long playerId, long participantId, byte frameScore) {
        this.frameNumber = frameNumber;
        this.rolls = rolls;
        this.playerName = playerName;
        this.currentScore = currentScore;
        this.playerId = playerId;
        this.participantId = participantId;
        this.frameScore = frameScore;
    }

    protected FrameOverview(Parcel in) {
        frameNumber = in.readByte();
        in.readList(rolls, Byte.class.getClassLoader());
        playerName = in.readString();
        currentScore = in.readInt();
        playerId = in.readLong();
        participantId = in.readLong();
        frameScore = in.readByte();
    }

    public static final Creator<FrameOverview> CREATOR = new Creator<FrameOverview>() {
        @Override
        public FrameOverview createFromParcel(Parcel in) {
            return new FrameOverview(in);
        }

        @Override
        public FrameOverview[] newArray(int size) {
            return new FrameOverview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(frameNumber);
        dest.writeList(rolls);
        dest.writeString(playerName);
        dest.writeInt(currentScore);
        dest.writeLong(playerId);
        dest.writeLong(participantId);
        dest.writeByte(frameScore);
    }

    public byte getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(byte frameNumber) {
        this.frameNumber = frameNumber;
    }

    public List<Byte> getRolls() {
        return rolls;
    }

    public void setRolls(List<Byte> rolls) {
        this.rolls = rolls;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public byte getFrameScore() {
        return frameScore;
    }

    public void setFrameScore(byte frameScore) {
        this.frameScore = frameScore;
    }
}
