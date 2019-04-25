package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tPLAYER_STATS)
public class PlayerStat extends fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat implements Parcelable {

    @DatabaseField(columnName = DBConstants.cGOALS)
    private int goals;

    @DatabaseField(columnName = DBConstants.cASSISTS)
    private int assists;

    @DatabaseField(columnName = DBConstants.cPLUS_MINUS)
    private int plus_minus;

    @DatabaseField(columnName = DBConstants.cSAVES)
    private int saves;

    public PlayerStat(PlayerStat p) {
        this.participant_id = p.participant_id;
        this.player_id = p.player_id;
        this.goals = p.goals;
        this.assists = p.assists;
        this.plus_minus = p.plus_minus;
        this.saves = p.saves;
    }

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
    }

    public PlayerStat(fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat p) {
        super(p.getId(), p.getPlayerId());
    }

    public PlayerStat(Parcel in) {
        super(in);
        goals = in.readInt();
        assists = in.readInt();
        plus_minus = in.readInt();
        saves = in.readInt();
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerStat)
            return id == ((PlayerStat)o).getId();

        return super.equals(o);
    }
}
