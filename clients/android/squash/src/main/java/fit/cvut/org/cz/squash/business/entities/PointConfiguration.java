package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.tmlibrary.business.entities.IEntity;

/**
 * Represents point configuration for tournament
 * methods return how much points should be awarded
 * Created by Vaclav on 19. 4. 2016.
 */
@DatabaseTable(tableName = SDBConstants.tPOINT_CONFIG)
public class PointConfiguration extends fit.cvut.org.cz.tmlibrary.business.entities.PointConfiguration implements Parcelable, IEntity {
    @DatabaseField(columnName = SDBConstants.cWIN)
    private int win;

    @DatabaseField(columnName = SDBConstants.cDRAW)
    private int draw;

    @DatabaseField(columnName = SDBConstants.cLOSS)
    private int loss;

    public PointConfiguration() {}

    public PointConfiguration(Parcel in) {
        this.tournamentId = in.readLong();
        this.win = in.readInt();
        this.draw = in.readInt();
        this.loss = in.readInt();
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
        dest.writeInt(win);
        dest.writeInt(draw);
        dest.writeInt(loss);
    }

    public PointConfiguration(long tournamentId, int nW, int nD, int nL) {
        this.tournamentId = tournamentId;
        this.win = nW;
        this.draw = nD;
        this.loss = nL;
    }

    @Override
    public long getId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public static PointConfiguration defaultConfig() {
        return new PointConfiguration(-1, 2, 1, 0);
    }

    public int getWin() {
        return win;
    }

    public int getDraw() {
        return draw;
    }

    public int getLoss() {
        return loss;
    }
}
