package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.squash.business.entities.communication.Constants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Class that holds aggregated statistics
 * Represents computed statistics for one pleyer across some set of data
 * Created by Vaclav on 7. 4. 2016.
 */
public class SAggregatedStats implements Parcelable, IEntity {
    public long playerId;
    public String playerName;

    public int games_played, points, won, lost, draws, setsWon, setsLost, ballsWon, ballsLost;
    public double setsWonAvg, setsLostAvg, ballsWonAvg, ballsLostAvg, matchWinRate, setsWinRate;
    public SAggregatedStats(String playerName, long playerId){
        this.playerName = playerName;
        this.playerId = playerId;
        games_played = points = won = lost = draws = setsWon = setsLost = ballsWon = ballsLost = 0;
        setsWonAvg = setsLostAvg = ballsLostAvg = ballsWonAvg = matchWinRate = setsWinRate = 0;
    }

    protected SAggregatedStats(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();
        games_played = in.readInt();
        points = in.readInt();
        won = in.readInt();
        lost = in.readInt();
        draws = in.readInt();
        setsWon = in.readInt();
        setsLost = in.readInt();
        ballsWon = in.readInt();
        ballsLost = in.readInt();
        setsWonAvg = in.readDouble();
        setsLostAvg = in.readDouble();
        ballsWonAvg = in.readDouble();
        ballsLostAvg = in.readDouble();
        matchWinRate = in.readDouble();
        setsWinRate = in.readDouble();
    }

    public static final Creator<SAggregatedStats> CREATOR = new Creator<SAggregatedStats>() {
        @Override
        public SAggregatedStats createFromParcel(Parcel in) {
            return new SAggregatedStats(in);
        }

        @Override
        public SAggregatedStats[] newArray(int size) {
            return new SAggregatedStats[size];
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
        dest.writeInt(games_played);
        dest.writeInt(points);
        dest.writeInt(won);
        dest.writeInt(lost);
        dest.writeInt(draws);
        dest.writeInt(setsWon);
        dest.writeInt(setsLost);
        dest.writeInt(ballsWon);
        dest.writeInt(ballsLost);
        dest.writeDouble(setsWonAvg);
        dest.writeDouble(setsLostAvg);
        dest.writeDouble(ballsWonAvg);
        dest.writeDouble(ballsLostAvg);
        dest.writeDouble(matchWinRate);
        dest.writeDouble(setsWinRate);
    }

    public double getStat(String key) {
        switch (key) {
            case Constants.MATCHES: return games_played;
            case Constants.POINTS: return points;
            case Constants.WINS: return won;
            case Constants.LOSSES: return lost;
            case Constants.DRAWS: return draws;
            case Constants.MATCH_WIN_RATE: return matchWinRate;
            case Constants.SETS_PER: return getSetsPer();
            case Constants.BALLS_PER: return getBallsPer();
            case Constants.BALLS: return ballsWon-ballsLost;
            case Constants.SETS: return setsWon-setsLost;
            default: return 0;
        }
    }

    public double getBallsPer() {
        if (ballsWon + ballsLost == 0)
            return 0;
        return (double)ballsWon/(ballsWon+ballsLost);
    }

    public double getSetsPer() {
        if (setsWon + setsLost == 0)
            return 0;
        return (double)setsWon/(setsWon+setsLost);
    }

    public int getTotalSets() {
        return setsWon + setsLost;
    }

    @Override
    public long getId() {
        return playerId;
    }
}
