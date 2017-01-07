package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Player Stat Record entity.
 */
public class PlayerAggregatedStatsRecord implements Parcelable {
    private String key;
    private String val;
    private int forPortrait;

    /**
     * Stat Record constructor
     * @param key stat record type
     * @param value stat record value
     * @param forPortrait true if value should be displayed in portrait
     */
    public PlayerAggregatedStatsRecord(String key, String value, boolean forPortrait) {
        this.key = key;
        this.val = value;
        this.forPortrait = forPortrait ? 1 : 0;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected PlayerAggregatedStatsRecord(Parcel in) {
        key = in.readString();
        val = in.readString();
        forPortrait = in.readInt();
    }

    /**
     * Parcelable creator.
     */
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

    /**
     * Key getter.
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Value getter.
     * @return value
     */
    public String getVal() {
        return val;
    }

    /**
     * For portrait getter.
     * @return forPortrait
     */
    public boolean getForPortrait() {
        return forPortrait == 1 ? true : false;
    }

}
