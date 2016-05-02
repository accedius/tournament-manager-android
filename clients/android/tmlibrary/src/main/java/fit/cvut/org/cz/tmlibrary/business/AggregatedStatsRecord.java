package fit.cvut.org.cz.tmlibrary.business;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 1.5.2016.
 */
public class AggregatedStatsRecord implements Parcelable {
    private String key;
    private String val;

    public AggregatedStatsRecord(String k, String v) {
        this.key = k;
        this.val = v;
    }

    protected AggregatedStatsRecord(Parcel in) {
        key = in.readString();
        val = in.readString();
    }

    public static final Parcelable.Creator<AggregatedStatsRecord> CREATOR = new Parcelable.Creator<AggregatedStatsRecord>() {
        @Override
        public AggregatedStatsRecord createFromParcel(Parcel in) {
            return new AggregatedStatsRecord(in);
        }

        @Override
        public AggregatedStatsRecord[] newArray(int size) {
            return new AggregatedStatsRecord[size];
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
    }

    public String getKey() {
        return key;
    }
    public String getVal() {
        return val;
    }
}
