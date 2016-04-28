package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class AgregatedStats implements Parcelable {

    public long playerId;
    public String playerName;

    public int won, lost, draws, setsWon, setsLost, ballsWon, ballsLost;
    public double setsWonAvg, setsLostAvg, ballsWonAvg, ballsLostAvg, matchWinRate, setsWinRate;
    public AgregatedStats(String playerName, long playerId){
        this.playerName = playerName;
        this.playerId = playerId;
        won = lost = draws = setsWon = setsLost = ballsWon = ballsLost = 0;
        setsWonAvg = setsLostAvg = ballsLostAvg = ballsWonAvg = matchWinRate = setsWinRate = 0;
    }

    protected AgregatedStats(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();
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

    public static final Creator<AgregatedStats> CREATOR = new Creator<AgregatedStats>() {
        @Override
        public AgregatedStats createFromParcel(Parcel in) {
            return new AgregatedStats(in);
        }

        @Override
        public AgregatedStats[] newArray(int size) {
            return new AgregatedStats[size];
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
}
