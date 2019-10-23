package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * PointConfiguration entity (describes how many points costs different standings in a match: how much for 1st, 2nd and 3rd place and on etc.) and its representation in database
 */
@DatabaseTable(tableName = DBConstants.tCONFIGURATIONS)
public class PointConfiguration implements Parcelable, IEntity {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    public long id;

    @DatabaseField(columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cTOURNAMENT_ID)
    public long tournamentId;

    @DatabaseField(columnName = DBConstants.cSIDES_NUMBER)
    public long sidesNumber;

    public List<Float> configurations = new ArrayList<>();

    public PointConfiguration() {}

    public PointConfiguration(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        sidesNumber = in.readLong();
        for (int i = 0; i<sidesNumber; i++) {
            configurations.add(in.readFloat());
        }
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tournamentId);
        dest.writeLong(sidesNumber);
        for (int i = 0; i<sidesNumber; i++) {
            dest.writeFloat((float) configurations.get(i));
        }
    }

    public PointConfiguration(long id, long tournamentId, long sidesNumber, List<Float> pointConfiguration) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.sidesNumber = sidesNumber;
        this.configurations.addAll(pointConfiguration.subList(0, (int) sidesNumber));
    }

    public PointConfiguration(long tournamentId, long sidesNumber, List<Float> pointConfiguration) {
        this.tournamentId = tournamentId;
        this.sidesNumber = sidesNumber;
        this.configurations.addAll(pointConfiguration.subList(0, (int) sidesNumber));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSidesNumber() {
        return sidesNumber;
    }

    public void setSidesNumber(long sidesNumber) {
        this.sidesNumber = sidesNumber;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public static PointConfiguration defaultConfig() {
        List<Float> defaultConfiguration = new ArrayList<>();
        defaultConfiguration.add(1f);
        defaultConfiguration.add(0f);
        return new PointConfiguration(-1, 2, defaultConfiguration);
    }
}
