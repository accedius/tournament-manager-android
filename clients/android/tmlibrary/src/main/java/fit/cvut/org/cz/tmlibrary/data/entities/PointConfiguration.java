package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 4.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tCONFIGURATIONS)
public class PointConfiguration implements Parcelable, IEntity{
    @DatabaseField(id = true, columnName = DBConstants.cTOURNAMENT_ID)
    public long tournamentId;

    protected PointConfiguration() {}

    protected PointConfiguration(Parcel in) {
        tournamentId = in.readLong();
    }

    public static final Creator<PointConfiguration> CREATOR = new Creator<PointConfiguration>() {
        @Override
        public PointConfiguration createFromParcel(Parcel in) {
            return new PointConfiguration(in);
        }

        @Override
        public PointConfiguration[] newArray(int size) {
            return new PointConfiguration[size];
        }
    };

    @Override
    public long getId() {
        return tournamentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tournamentId);
    }
}
