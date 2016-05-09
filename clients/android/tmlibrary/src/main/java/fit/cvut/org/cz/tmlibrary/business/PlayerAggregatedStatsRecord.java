package fit.cvut.org.cz.tmlibrary.business;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 1.5.2016.
 */
public class PlayerAggregatedStatsRecord implements Parcelable {
    private String key;
    private String val;
    private int forPortrait;

    public PlayerAggregatedStatsRecord(String k, String v, boolean forPortrait) {
        this.key = k;
        this.val = v;
        this.forPortrait = forPortrait ? 1 : 0;
    }

    protected PlayerAggregatedStatsRecord(Parcel in) {
        key = in.readString();
        val = in.readString();
        forPortrait = in.readInt();
    }

    public static final Parcelable.Creator<PlayerAggregatedStatsRecord> CREATOR = new Parcelable.Creator<PlayerAggregatedStatsRecord>() {
        @Override
        public PlayerAggregatedStatsRecord createFromParcel(Parcel in) {
            return new PlayerAggregatedStatsRecord(in);
        }

        @Override
        public PlayerAggregatedStatsRecord[] newArray(int size) {
            return new PlayerAggregatedStatsRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(val);
        dest.writeInt(forPortrait);
    }

    public String getKey() {
        return key;
    }
    public String getVal() {
        return val;
    }
    public boolean getForPortrait() {
        return forPortrait == 1 ? true : false;
    }

}
