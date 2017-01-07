package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Player Aggregated Stats entity.
 */
public class PlayerAggregatedStats implements Parcelable {
    /**
     * List of Player Stat records.
     */
    protected List<PlayerAggregatedStatsRecord> records = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public PlayerAggregatedStats() {}

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected PlayerAggregatedStats(Parcel in) {
        in.readTypedList(records, PlayerAggregatedStatsRecord.CREATOR);
    }

    /**
     * Method for adding stat record to records.
     * @param record record to be added
     */
    public void addRecord(PlayerAggregatedStatsRecord record) {
        if (record == null)
            return;

        records.add(record);
    }

    /**
     * Parcelable creator.
     */
    public static final Parcelable.Creator<PlayerAggregatedStats> CREATOR = new Parcelable.Creator<PlayerAggregatedStats>() {
        @Override
        public PlayerAggregatedStats createFromParcel(Parcel in) {
            return new PlayerAggregatedStats(in);
        }

        @Override
        public PlayerAggregatedStats[] newArray(int size) {
            return new PlayerAggregatedStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(records);
    }

    /**
     * Stat records getter.
     * @return list of records.
     */
    public List<PlayerAggregatedStatsRecord> getRecords() {
        return records;
    }
}
