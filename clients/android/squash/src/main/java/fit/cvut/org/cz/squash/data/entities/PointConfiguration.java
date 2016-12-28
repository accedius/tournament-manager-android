package fit.cvut.org.cz.squash.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.squash.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Represents point configuration for tournament
 * methods return how much points should be awarded
 * Created by Vaclav on 19. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tPOINT_CONFIG)
public class PointConfiguration extends fit.cvut.org.cz.tmlibrary.data.entities.PointConfiguration implements Parcelable, IEntity {
    @DatabaseField(columnName = DBConstants.cWIN)
    private int win;

    @DatabaseField(columnName = DBConstants.cDRAW)
    private int draw;

    @DatabaseField(columnName = DBConstants.cLOSS)
    private int loss;

    public PointConfiguration() {}

    public PointConfiguration(Parcel in) {
        super(in);
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
        super.writeToParcel(dest, flags);
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
