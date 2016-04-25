package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 17. 4. 2016.
 */
public class StandingItem implements Parcelable {

    private String name;
    private int wins, loses, draws, setsWon, setsLost, points;

    public StandingItem(String name){
        this.name = name;
        wins = loses = draws = setsWon = setsLost = points = 0;
    }

    public StandingItem(String name, int wins, int loses, int draws, int setsWon, int setsLost, int points) {
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.draws = draws;
        this.setsWon = setsWon;
        this.setsLost = setsLost;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(int setsWon) {
        this.setsWon = setsWon;
    }

    public int getSetsLost() {
        return setsLost;
    }

    public void setSetsLost(int setsLost) {
        this.setsLost = setsLost;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
