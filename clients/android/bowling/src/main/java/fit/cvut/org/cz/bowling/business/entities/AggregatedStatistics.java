package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Class for handling statistics
 */
public class AggregatedStatistics implements Parcelable, IEntity {
    private long playerId;
    private String playerName;

    private long strikes, spares, points, matchPoints, matches;
    private double avgStrikes, avgPoints, avgMatchPoints;

    private void calcAvg() {
        //this.points = this.strikes + this.spares;
        if (this.matches > 0) {
            this.avgStrikes = (double)this.strikes/(double)this.matches;
            this.avgPoints = (double)this.points/(double)this.matches;
            this.avgMatchPoints = (double)this.matchPoints /(double)this.matches;
        } else {
            this.avgStrikes = 0;
            this.avgPoints = 0;
            this.avgMatchPoints = 0;
        }
    }

    public AggregatedStatistics(long pID, String pName, long matches, long strikes, long spares, long points, long matchPoints) {
        this.playerId = pID;
        this.playerName = pName;
        this.matches = matches;
        this.strikes = strikes;
        this.spares = spares;
        this.points = points;
        this.matchPoints = matchPoints;
        calcAvg();
    }

    public static final Creator<AggregatedStatistics> CREATOR = new Creator<AggregatedStatistics>() {
        @Override
        public AggregatedStatistics createFromParcel(Parcel in) {
            return new AggregatedStatistics(in);
        }

        @Override
        public AggregatedStatistics[] newArray(int size) {
            return new AggregatedStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(playerId);
        dest.writeString(playerName);
        dest.writeLong(strikes);
        dest.writeLong(spares);
        dest.writeLong(points);
        dest.writeLong(matchPoints);
        dest.writeLong(matches);
        dest.writeDouble(avgStrikes);
        dest.writeDouble(avgPoints);
        dest.writeDouble(avgMatchPoints);
    }

    public AggregatedStatistics(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();

        strikes = in.readLong();
        spares = in.readLong();
        points = in.readLong();
        matchPoints = in.readLong();
        matches = in.readLong();

        avgStrikes = in.readDouble();
        avgPoints = in.readDouble();
        avgMatchPoints = in.readDouble();

    }

    public long getPlayerId() { return playerId; }

    public void setPlayerId(long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public long getStrikes() { return strikes; }

    public void setStrikes(long value) { this.strikes = value; }

    public long getSpares() { return spares; }

    public void setSpares(long value) { this.spares = value; }

    public long getPoints() { return points; }

    public void setPoints(long points) { this.points = points; }

    public long getMatchPoints() { return matchPoints; }

    public void setMatchPoints(long matchPoints) { this.matchPoints = matchPoints; }

    public long getMatches() { return matches; }

    public void setMatches(long matches) { this.matches = matches; }

    public double getAvgStrikes() { return avgStrikes; }

    public void setAvgStrikes(long avgStrikes) { this.avgStrikes = avgStrikes; }

    public double getAvgPoints() { return avgPoints; }

    public void setAvgPoints(long avgPoints) { this.avgPoints = avgPoints; }

    public double getAvgMatchPoints() { return avgMatchPoints; }

    public void setAvgMatchPoints(long avgMatchPoints) { this.avgMatchPoints = avgMatchPoints; }

    public double getStat(String key) {
        switch (key) {
            case Constants.MATCHES: return getMatches();
            case Constants.STRIKES: return getStrikes();
            case Constants.SPARES: return getSpares();
            case Constants.POINTS: return getPoints();
            case Constants.MATCH_POINTS: return getMatchPoints();
            case Constants.STRIKES_AVG: return getAvgStrikes();
            case Constants.POINTS_AVG: return getAvgPoints();
            case Constants.MATCH_POINTS_AVG: return getAvgMatchPoints();
            default: return 0;
        }
    }

    @Override
    public long getId() {
        return playerId;
    }
}
