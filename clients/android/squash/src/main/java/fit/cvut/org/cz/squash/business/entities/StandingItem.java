package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class holds how individual participant does in tournament
 * this defines order
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingItem implements Parcelable {
    public String name;
    public int wins, losses, draws, setsWon, setsLost, points;

    public StandingItem(String name){
        this.name = name;
        wins = losses = draws = setsWon = setsLost = points = 0;
    }

    protected StandingItem(Parcel in) {
        name = in.readString();
        wins = in.readInt();
        losses = in.readInt();
        draws = in.readInt();
        setsWon = in.readInt();
        setsLost = in.readInt();
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
        dest.writeString(name);
        dest.writeInt(wins);
        dest.writeInt(losses);
        dest.writeInt(draws);
        dest.writeInt(setsWon);
        dest.writeInt(setsLost);
        dest.writeInt(points);
    }

    public double getStat(String key) {
        switch (key) {
            case "p": return points;
            case "w": return wins;
            case "l": return losses;
            case "d": return draws;
            case "sw": return setsWon;
            case "sl": return setsLost;
            default: return 0;
        }
    }
}
