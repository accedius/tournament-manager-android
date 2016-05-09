package fit.cvut.org.cz.tmlibrary.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 28.4.2016.
 */


public class PlayerAggregatedStats implements Parcelable {
    protected List<PlayerAggregatedStatsRecord> records = new ArrayList<>();

    protected PlayerAggregatedStats(Parcel in) {
        in.readTypedList(records, PlayerAggregatedStatsRecord.CREATOR);
    }

    public PlayerAggregatedStats() {
        records = new ArrayList<>();
    }

    public void addRecord(PlayerAggregatedStatsRecord record) {
        if (record == null)
            return;

        records.add(record);
    }

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

    public List<PlayerAggregatedStatsRecord> getRecords() {
        return records;
    }
}
