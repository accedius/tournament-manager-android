package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.serialization.Constants;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.ShareBase;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * PointConfiguration entity (describes how many points costs different standings in a match: how much for 1st, 2nd and 3rd place and on etc.) and its representation in database
 */
@DatabaseTable(tableName = DBConstants.tCONFIGURATIONS)
public class PointConfiguration extends ShareBase implements Parcelable, IEntity {
    @DatabaseField(generatedId = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cID)
    public long id;

    @DatabaseField(columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cTOURNAMENT_ID)
    public long tournamentId;

    @DatabaseField(columnName = DBConstants.cSIDES_NUMBER)
    public long sidesNumber;

    @DatabaseField(columnName = DBConstants.cPLACE_POINTS)
    public String placePoints;

    public List<Float> configurationPlacePoints = new ArrayList<>();

    public PointConfiguration() {}

    @Override
    public String getEntityType() {
        return Constants.POINT_CONFIGURATION;
    }

    public PointConfiguration(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        sidesNumber = in.readLong();
        placePoints = in.readString();
        in.readList(configurationPlacePoints, Float.class.getClassLoader());
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
        dest.writeString(placePoints);
        dest.writeList(configurationPlacePoints);
    }

    public PointConfiguration(long id, long tournamentId, long sidesNumber, List<Float> pointConfiguration) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.sidesNumber = sidesNumber;
        setConfigurationPlacePoints(pointConfiguration);
    }

    public PointConfiguration(long id, String uid, long tournamentId, long sidesNumber, List<Float> pointConfiguration) {
        this.id = id;
        this.uid = uid;
        this.tournamentId = tournamentId;
        this.sidesNumber = sidesNumber;
        setConfigurationPlacePoints(pointConfiguration);
    }

    public PointConfiguration(long tournamentId, long sidesNumber, List<Float> pointConfiguration) {
        this.tournamentId = tournamentId;
        this.sidesNumber = sidesNumber;
        setConfigurationPlacePoints(pointConfiguration);
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

    public void updateConfigurationPlacePoints() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Float>>() {}.getType();
        configurationPlacePoints = gson.fromJson(placePoints, type);
    }

    public List<Float> getConfigurationPlacePoints() {
        if(configurationPlacePoints.isEmpty()) {
            updateConfigurationPlacePoints();
        }
        return configurationPlacePoints;
    }

    public void setConfigurationPlacePoints(List<Float> placePoints) {
        this.configurationPlacePoints = placePoints;
        Gson gson = new Gson();
        this.placePoints = gson.toJson(placePoints);
    }

    public static PointConfiguration defaultConfig() {
        List<Float> defaultConfiguration = new ArrayList<>();
        defaultConfiguration.add(1f);
        defaultConfiguration.add(0f);
        return new PointConfiguration(-1, 2, defaultConfiguration);
    }
}
