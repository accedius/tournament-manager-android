package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

@DatabaseTable(tableName = DBConstants.tCONFIGURATIONS)
public class PointConfiguration extends fit.cvut.org.cz.tmlibrary.data.entities.PointConfiguration implements Parcelable, IEntity {
    @DatabaseField(columnName = DBConstants.cNTW)
    public int ntW;

    @DatabaseField(columnName = DBConstants.cNTD)
    public int ntD;

    @DatabaseField(columnName = DBConstants.cNTL)
    public int ntL;

    @DatabaseField(columnName = DBConstants.cOTW)
    public int otW;

    @DatabaseField(columnName = DBConstants.cOTD)
    public int otD;

    @DatabaseField(columnName = DBConstants.cOTL)
    public int otL;

    @DatabaseField(columnName = DBConstants.cSOW)
    public int soW;

    @DatabaseField(columnName = DBConstants.cSOL)
    public int soL;

    public PointConfiguration() {}

    public PointConfiguration(Parcel in) {
        super(in);
        this.ntW = in.readInt();
        this.ntD = in.readInt();
        this.ntL = in.readInt();

        this.otW = in.readInt();
        this.otD = in.readInt();
        this.otL = in.readInt();

        this.soW = in.readInt();
        this.soL = in.readInt();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(ntW);
        dest.writeInt(ntD);
        dest.writeInt(ntL);

        dest.writeInt(otW);
        dest.writeInt(otD);
        dest.writeInt(otL);

        dest.writeInt(soW);
        dest.writeInt(soL);
    }

    public PointConfiguration(long tournamentId, int nW, int nD, int nL, int oW, int oD, int oL, int sW, int sL) {
        this.tournamentId = tournamentId;
        this.ntW = nW;
        this.ntD = nD;
        this.ntL = nL;

        this.otW = oW;
        this.otD = oD;
        this.otL = oL;

        this.soW = sW;
        this.soL = sL;
    }

    @Override
    public long getId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public static PointConfiguration defaultConfig() {
        return new PointConfiguration(-1, 3, 1, 0, 2, 1, 1, 2, 1);
    }
}
