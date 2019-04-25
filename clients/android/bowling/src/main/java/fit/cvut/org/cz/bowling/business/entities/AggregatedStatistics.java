package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

public class AggregatedStatistics implements Parcelable, IEntity {
    private long playerId;
    private String playerName;

    private long goals, assists, points, plusMinusPoints, teamPoints, matches, wins, losses, draws, saves;
    private double avgGoals, avgPoints, avgTeamPoints;

    private void calcAvg() {
        this.points = this.goals + this.assists;
        if (this.matches > 0) {
            this.avgGoals = (double)this.goals/(double)this.matches;
            this.avgPoints = (double)this.points/(double)this.matches;
            this.avgTeamPoints = (double)this.teamPoints/(double)this.matches;
        } else {
            this.avgGoals = 0;
            this.avgPoints = 0;
            this.avgTeamPoints = 0;
        }
    }

    public AggregatedStatistics(long pID, String pName, long matches, long wins, long draws, long losses, long goals, long assists, long plusMinusPoints, long teamPoints, long saves) {
        this.playerId = pID;
        this.playerName = pName;
        this.matches = matches;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goals = goals;
        this.assists = assists;
        this.plusMinusPoints = plusMinusPoints;
        this.teamPoints = teamPoints;
        this.saves = saves;
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
        dest.writeLong(goals);
        dest.writeLong(assists);
        dest.writeLong(points);
        dest.writeLong(plusMinusPoints);
        dest.writeLong(teamPoints);
        dest.writeLong(matches);
        dest.writeLong(wins);
        dest.writeLong(losses);
        dest.writeLong(draws);
        dest.writeDouble(avgGoals);
        dest.writeDouble(avgPoints);
        dest.writeDouble(avgTeamPoints);
        dest.writeLong(saves);
    }

    public AggregatedStatistics(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();

        goals = in.readLong();
        assists = in.readLong();
        points = in.readLong();
        plusMinusPoints = in.readLong();
        teamPoints = in.readLong();
        matches = in.readLong();
        wins = in.readLong();
        losses = in.readLong();
        draws = in.readLong();

        avgGoals = in.readDouble();
        avgPoints = in.readDouble();
        avgTeamPoints = in.readDouble();

        saves = in.readLong();
    }

    public long getPlayerId() { return playerId; }

    public void setPlayerId(long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public long getGoals() { return goals; }

    public void setGoals(long goals) { this.goals = goals; }

    public long getAssists() { return assists; }

    public void setAssists(long assists) { this.assists = assists; }

    public long getPoints() { return points; }

    public void setPoints(long points) { this.points = points; }

    public long getPlusMinusPoints() { return plusMinusPoints; }

    public void setPlusMinusPoints(long plusMinusPoints) { this.plusMinusPoints = plusMinusPoints; }

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

    public double getAvgGoals() { return avgGoals; }

    public void setAvgGoals(long avgGoals) { this.avgGoals = avgGoals; }

    public double getAvgPoints() { return avgPoints; }

    public void setAvgPoints(long avgPoints) { this.avgPoints = avgPoints; }

    public double getAvgTeamPoints() { return avgTeamPoints; }

    public void setAvgTeamPoints(long avgTeamPoints) { this.avgTeamPoints = avgTeamPoints; }

    public long getSaves() {
        return saves;
    }

    public void setSaves(long saves) {
        this.saves = saves;
    }

    public double getAvgWins() {
        if (matches == 0)
            return 0;
        return wins/matches;
    }

    public double getStat(String key) {
        switch (key) {
            case Constants.MATCHES: return getMatches();
            case Constants.GOALS: return getGoals();
            case Constants.ASSISTS: return getAssists();
            case Constants.POINTS: return getPoints();
            case Constants.PLUS_MINUS: return getPlusMinusPoints();
            case Constants.SAVES: return getSaves();
            case Constants.WINS: return getWins();
            case Constants.DRAWS: return getDraws();
            case Constants.LOSSES: return getLosses();
            case Constants.TEAM_POINTS: return getTeamPoints();
            case Constants.GOALS_AVG: return getAvgGoals();
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
