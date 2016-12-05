package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 2.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYER_STATS)
public class PlayerStat extends fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat implements Parcelable {

    @DatabaseField(columnName = HockeyDBConstants.cGOALS)
    private int goals;

    @DatabaseField(columnName = HockeyDBConstants.cASSISTS)
    private int assists;

    @DatabaseField(columnName = HockeyDBConstants.cPLUS_MINUS)
    private int plus_minus;

    @DatabaseField(columnName = HockeyDBConstants.cSAVES)
    private int saves;

    private String name;

    public PlayerStat() {}

    public PlayerStat(long participantId, long playerId) {
        super(participantId, playerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(goals);
        dest.writeInt(assists);
        dest.writeInt(plus_minus);
        dest.writeInt(saves);
        dest.writeString(name);
    }

    public PlayerStat(fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat p) {
        super(p.getId(), p.getPlayerId());
    }

    public PlayerStat(Parcel in) {
        super(in);
        goals = in.readInt();
        assists = in.readInt();
        plus_minus = in.readInt();
        saves = in.readInt();
        name = in.readString();
    }

    public static final Creator<PlayerStat> CREATOR = new Creator<PlayerStat>() {
        @Override
        public PlayerStat createFromParcel(Parcel in) {
            return new PlayerStat(in);
        }

        @Override
        public PlayerStat[] newArray(int size) {
            return new PlayerStat[size];
        }
    };

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getPlusMinus() {
        return plus_minus;
    }

    public void setPlusMinus(int plus_minus) {
        this.plus_minus = plus_minus;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerStat)
            return id == ((PlayerStat)o).getId();

        return super.equals(o);
    }
}
