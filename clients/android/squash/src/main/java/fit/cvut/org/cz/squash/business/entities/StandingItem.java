package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.squash.business.entities.communication.Constants;

/**
 * Class holds how individual participant does in tournament
 * this defines order
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingItem implements Parcelable {
    public long id;
    public String name;
    public int wins, losses, draws, setsWon, setsLost, ballsWon, ballsLost, points;

    public StandingItem(long id, String name){
        this.id = id;
        this.name = name;
        wins = losses = draws = setsWon = setsLost = ballsWon = ballsLost = points = 0;
    }

    protected StandingItem(Parcel in) {
        id = in.readLong();
        name = in.readString();
        wins = in.readInt();
        losses = in.readInt();
        draws = in.readInt();
        setsWon = in.readInt();
        setsLost = in.readInt();
        ballsWon = in.readInt();
        ballsLost = in.readInt();
        points = in.readInt();
    }

    public static final Creator<StandingItem> CREATOR = new Creator<StandingItem>() {
        @Override
        public StandingItem createFromParcel(Parcel in) {
            return new StandingItem(in);
        }

        @Override
        public StandingItem[] newArray(int size) {
            return new StandingItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(wins);
        dest.writeInt(losses);
        dest.writeInt(draws);
        dest.writeInt(setsWon);
        dest.writeInt(setsLost);
        dest.writeInt(ballsWon);
        dest.writeInt(ballsLost);
        dest.writeInt(points);
    }

    public double getStat(String key) {
        switch (key) {
            case Constants.POINTS: return points;
            case Constants.WINS: return wins;
            case Constants.LOSSES: return losses;
            case Constants.DRAWS: return draws;
            case Constants.SETS: return setsWon-setsLost;
            case Constants.MATCHES: return wins+losses+draws;
            case Constants.MATCH_WIN_RATE: return getMatchPer();
            case Constants.SETS_PER: return getSetsPer();
            case Constants.BALLS_PER: return getBallsPer();
            case Constants.BALLS: return ballsWon-ballsLost;
            default: return 0;
        }
    }

    public double getMatchPer() {
        if (wins + losses == 0)
            return 0;
        return (double)wins/(wins+losses);
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

    public int getMatches() {
        return wins + draws + losses;
    }
}
