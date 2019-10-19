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

    private long strikes, spares, points, teamPoints, matches, wins, losses, draws;
    private double avgStrikes, avgPoints, avgTeamPoints;

    private void calcAvg() {
        //this.points = this.strikes + this.spares;
        if (this.matches > 0) {
            this.avgStrikes = (double)this.strikes/(double)this.matches;
            this.avgPoints = (double)this.points/(double)this.matches;
            this.avgTeamPoints = (double)this.teamPoints/(double)this.matches;
        } else {
            this.avgStrikes = 0;
            this.avgPoints = 0;
            this.avgTeamPoints = 0;
        }
    }

    public AggregatedStatistics(long pID, String pName, long matches, long wins, long draws, long losses, long strikes, long spares, long points, long teamPoints) {
        this.playerId = pID;
        this.playerName = pName;
        this.matches = matches;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.strikes = strikes;
        this.spares = spares;
        this.points = points;
        this.teamPoints = teamPoints;
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
        dest.writeLong(teamPoints);
        dest.writeLong(matches);
        dest.writeLong(wins);
        dest.writeLong(losses);
        dest.writeLong(draws);
        dest.writeDouble(avgStrikes);
        dest.writeDouble(avgPoints);
        dest.writeDouble(avgTeamPoints);
    }

    public AggregatedStatistics(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();

        strikes = in.readLong();
        spares = in.readLong();
        points = in.readLong();
        teamPoints = in.readLong();
        matches = in.readLong();
        wins = in.readLong();
        losses = in.readLong();
        draws = in.readLong();

        avgStrikes = in.readDouble();
        avgPoints = in.readDouble();
        avgTeamPoints = in.readDouble();

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

    public long getTeamPoints() { return teamPoints; }

    public void setTeamPoints(long teamPoints) { this.teamPoints = teamPoints; }

    public long getMatches() { return matches; }

    public void setMatches(long matches) { this.matches = matches; }

    public long getWins() { return wins; }

    public void setWins(long wins) { this.wins = wins; }

    public long getLosses() { return losses; }

    public void setLosses(long losses) { this.losses = losses; }

    public long getDraws() { return draws; }

    public void setDraws(long draws) { this.draws = draws; }

    public double getAvgStrikes() { return avgStrikes; }

    public void setAvgStrikes(long avgStrikes) { this.avgStrikes = avgStrikes; }

    public double getAvgPoints() { return avgPoints; }

    public void setAvgPoints(long avgPoints) { this.avgPoints = avgPoints; }

    public double getAvgTeamPoints() { return avgTeamPoints; }

    public void setAvgTeamPoints(long avgTeamPoints) { this.avgTeamPoints = avgTeamPoints; }

    public double getAvgWins() {
        if (matches == 0)
            return 0;
        return wins/matches;
    }

    public double getStat(String key) {
        switch (key) {
            case Constants.MATCHES: return getMatches();
            case Constants.STRIKES: return getStrikes();
            case Constants.SPARES: return getSpares();
            case Constants.POINTS: return getPoints();
            case Constants.WINS: return getWins();
            case Constants.DRAWS: return getDraws();
            case Constants.LOSSES: return getLosses();
            case Constants.TEAM_POINTS: return getTeamPoints();
            case Constants.STRIKES_AVG: return getAvgStrikes();
            case Constants.POINTS_AVG: return getAvgPoints();
            case Constants.TEAM_POINTS_AVG: return getAvgTeamPoints();
            default: return 0;
        }
    }

    @Override
    public long getId() {
        return playerId;
    }
}
