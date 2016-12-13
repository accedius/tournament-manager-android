package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.tmlibrary.business.entities.interfaces.IEntity;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
// TODO point configuration is not exported and imported
@DatabaseTable(tableName = HockeyDBConstants.tCONFIGURATIONS)
public class PointConfiguration extends fit.cvut.org.cz.tmlibrary.business.entities.PointConfiguration implements Parcelable, IEntity {
    @DatabaseField(columnName = HockeyDBConstants.cNTW)
    public int ntW;

    @DatabaseField(columnName = HockeyDBConstants.cNTD)
    public int ntD;

    @DatabaseField(columnName = HockeyDBConstants.cNTL)
    public int ntL;

    @DatabaseField(columnName = HockeyDBConstants.cOTW)
    public int otW;

    @DatabaseField(columnName = HockeyDBConstants.cOTD)
    public int otD;

    @DatabaseField(columnName = HockeyDBConstants.cOTL)
    public int otL;

    @DatabaseField(columnName = HockeyDBConstants.cSOW)
    public int soW;

    @DatabaseField(columnName = HockeyDBConstants.cSOL)
    public int soL;

    public PointConfiguration() {}

    public PointConfiguration(Parcel in) {
        this.tournamentId = in.readLong();
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
        dest.writeLong(tournamentId);
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
