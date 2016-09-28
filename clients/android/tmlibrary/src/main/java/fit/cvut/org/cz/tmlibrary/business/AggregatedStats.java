package fit.cvut.org.cz.tmlibrary.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 28.4.2016.
 */

public class AggregatedStats implements Parcelable {
    protected List<PlayerAggregatedStats> records = new ArrayList<>();

    protected AggregatedStats(Parcel in) {
        in.readTypedList(records, PlayerAggregatedStats.CREATOR);
    }

    public AggregatedStats() {
        records = new ArrayList<>();
    }

    public void addPlayerStats(PlayerAggregatedStats record) {
        if (record == null)
            return;

        records.add(record);
    }

    public static final Parcelable.Creator<AggregatedStats> CREATOR = new Parcelable.Creator<AggregatedStats>() {
        @Override
        public AggregatedStats createFromParcel(Parcel in) {
            return new AggregatedStats(in);
        }

        @Override
        public AggregatedStats[] newArray(int size) {
            return new AggregatedStats[size];
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

    public List<PlayerAggregatedStats> getRecords() {
        return records;
    }
}
