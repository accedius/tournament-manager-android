package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class SetRowItem implements Parcelable {

    private int homeScore, awayScore;

    public SetRowItem() {
        homeScore = 0;
        awayScore = 0;
    }

    protected SetRowItem(Parcel in) {
        homeScore = in.readInt();
        awayScore = in.readInt();
    }

    public static final Creator<SetRowItem> CREATOR = new Creator<SetRowItem>() {
        @Override
        public SetRowItem createFromParcel(Parcel in) {
            return new SetRowItem(in);
        }

        @Override
        public SetRowItem[] newArray(int size) {
            return new SetRowItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}
