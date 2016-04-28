package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingItem implements Parcelable {

    public String name;
    public int wins, loses, draws, setsWon, setsLost, points;

    public StandingItem(String name){
        this.name = name;
        wins = loses = draws = setsWon = setsLost = points = 0;
    }

    protected StandingItem(Parcel in) {
        name = in.readString();
        wins = in.readInt();
        loses = in.readInt();
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
        dest.writeInt(loses);
        dest.writeInt(draws);
        dest.writeInt(setsWon);
        dest.writeInt(setsLost);
        dest.writeInt(points);
    }
}
