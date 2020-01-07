package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;

/**
 * PlayerStat entity (global individual player stats) and its representation in database
 */
@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tPLAYER_STATS)
public class PlayerStat extends fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat implements Parcelable {

    @DatabaseField(columnName = DBConstants.cSTRIKES)
    private int strikes;

    @DatabaseField(columnName = DBConstants.cSPARES)
    private int spares;

    @DatabaseField(columnName = DBConstants.cPOINTS)
    private int points;

    private String participantName;

    public PlayerStat(PlayerStat p) {
        this.participant_id = p.participant_id;
        this.player_id = p.player_id;
        this.strikes = p.strikes;
        this.spares = p.spares;
        this.points = p.points;
        this.participantName = p.participantName;
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
        dest.writeInt(strikes);
        dest.writeInt(spares);
        dest.writeInt(points);
    }

    public PlayerStat(fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat p) {
        super(p.getId(), p.getPlayerId());
    }

    public PlayerStat(Parcel in) {
        super(in);
        strikes = in.readInt();
        spares = in.readInt();
        points = in.readInt();
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

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public int getSpares() {
        return spares;
    }

    public void setSpares(int spares) {
        this.spares = spares;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerStat)
            return id == ((PlayerStat)o).getId();

        return super.equals(o);
    }
}
