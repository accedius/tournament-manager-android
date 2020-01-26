package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerStatOverview implements Parcelable {
    private long participantId, playerId;
    private int strikes, spares, points, frames;
    private String name;

    public PlayerStatOverview() {
    }

    public PlayerStatOverview(PlayerStatOverview overview) {
        participantId = overview.getParticipantId();
        playerId = overview.getPlayerId();
        strikes = overview.getStrikes();
        spares = overview.getSpares();
        points = overview.getPoints();
        frames = overview.getFrames();
        name = overview.getName();
    }

    public PlayerStatOverview(Parcel in) {
        participantId = in.readLong();
        playerId = in.readLong();
        strikes = in.readInt();
        spares = in.readInt();
        points = in.readInt();
        frames = in.readInt();
        name = in.readString();
    }

    public static final Creator<PlayerStatOverview> CREATOR = new Creator<PlayerStatOverview>() {
        @Override
        public PlayerStatOverview createFromParcel(Parcel in) {
            return new PlayerStatOverview(in);
        }

        @Override
        public PlayerStatOverview[] newArray(int size) {
            return new PlayerStatOverview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(participantId);
        dest.writeLong(playerId);
        dest.writeInt(strikes);
        dest.writeInt(spares);
        dest.writeInt(points);
        dest.writeInt(frames);
        dest.writeString(name);
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public int getSpares() {
        return spares;
    }

    public void setSpares(int spares) {
        this.spares = spares;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
