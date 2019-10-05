package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;

/**
 * Match entity and its representation in database
 */
@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.data.entities.Match implements Parcelable {
    @DatabaseField(columnName = DBConstants.cSHOOTOUTS)
    private boolean shootouts;

    @DatabaseField(columnName = DBConstants.cOVERTIME)
    private boolean overtime;

    public Match() {}
    public Match(long id, long tournamentId, Date date, boolean played, String note, int period, int round, boolean shootouts, boolean overtime) {
        super(id, tournamentId, date, played, note, period, round);
        this.shootouts = shootouts;
        this.overtime = overtime;
    }

    public Match(fit.cvut.org.cz.tmlibrary.data.entities.Match m) {
        super(m);
        shootouts = false;
        overtime = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (shootouts ? 1 : 0));
        dest.writeByte((byte) (overtime ? 1 : 0));
    }

    public Match(Parcel in) {
        super(in);
        shootouts = in.readByte() != 0;
        overtime = in.readByte() != 0;
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public boolean isShootouts() {
        return shootouts;
    }

    public void setShootouts(boolean shootouts) {
        this.shootouts = shootouts;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public String getHomeName() {
        return "";
    }

    public String getAwayName() {
        return "";
    }

    public long getHomeParticipantId() {
        for (Participant participant : participants) {
            if (ParticipantType.home.toString().equals(participant.getRole()))
                return participant.getParticipantId();
        }
        return -1;
    }

    public long getAwayParticipantId() {
        for (Participant participant : participants) {
            if (ParticipantType.away.toString().equals(participant.getRole()))
                return participant.getParticipantId();
        }
        return -1;
    }

    public List<PlayerStat> getHomePlayers() {
        return new LinkedList<>();
    }

    public List<PlayerStat> getAwayPlayers() {
        return new LinkedList<>();
    }
}
