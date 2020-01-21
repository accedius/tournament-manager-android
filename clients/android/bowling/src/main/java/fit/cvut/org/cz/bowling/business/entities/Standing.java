package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.bowling.business.entities.communication.Constants;

/**
 * Class for handling standings
 */
public class Standing implements Parcelable {
    private String name;
    private int matches, matchPoints, strikes, spares, points;
    private long teamId;

    public Standing() {}

    public Standing(String name, long teamId) {
        this(name, 0, 0, 0, 0, 0, teamId);
    }

    public Standing(String name, int matches, int matchPoints, int strikes, int spares, int points, long teamId) {
        this.name = name;
        this.matches = matches;
        this.matchPoints = matchPoints;
        this.strikes = strikes;
        this.spares = spares;
        this.points = points;
        this.teamId = teamId;
    }

    public void addMatchPoints(long points) { this.matchPoints += points; }
    public void addPoints(long points) { this.points += points; }

    public int getMatches() {
        return matches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMatchPoints() { return matchPoints; }

    public int getStrikes() { return strikes; }

    public int getSpares() { return spares; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(matches);
        dest.writeInt(matchPoints);
        dest.writeInt(strikes);
        dest.writeInt(spares);
        dest.writeInt(points);
        dest.writeLong(teamId);
    }

    public Standing(Parcel in) {
        this.name = in.readString();
        this.points = in.readInt();
        this.matchPoints = in.readInt();
        this.strikes = in.readInt();
        this.spares = in.readInt();
        this.points = in.readInt();
        this.teamId = in.readLong();
    }

    public static final Creator<Standing> CREATOR = new Creator<Standing>() {
        @Override
        public Standing createFromParcel(Parcel in) {
            return new Standing(in);
        }

        @Override
        public Standing[] newArray(int size) {
            return new Standing[size];
        }
    };

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public double getStat(String key) {
        switch (key) {
            case Constants.MATCHES: return getMatches();
            case Constants.POINTS: return getPoints();
            case Constants.MATCH_POINTS: return getMatchPoints();
            case Constants.STRIKES: return getStrikes();
            case Constants.SPARES: return getSpares();
            default: return 0;
        }
    }
}
