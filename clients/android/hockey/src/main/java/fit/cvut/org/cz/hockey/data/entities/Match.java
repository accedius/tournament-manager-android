package fit.cvut.org.cz.hockey.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.hockey.data.helpers.HockeyDBConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.data.entities.Match implements Parcelable {
    @DatabaseField(columnName = HockeyDBConstants.cSHOOTOUTS)
    private boolean shootouts;

    @DatabaseField(columnName = HockeyDBConstants.cOVERTIME)
    private boolean overtime;

    private String homeName;
    private String awayName;
    private List<PlayerStat> homePlayers = new ArrayList<>();
    private List<PlayerStat> awayPlayers = new ArrayList<>();

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
        dest.writeTypedList(homePlayers);
        dest.writeTypedList(awayPlayers);
    }

    public Match(Parcel in) {
        super(in);
        shootouts = in.readByte() != 0;
        overtime = in.readByte() != 0;
        in.readTypedList(homePlayers, PlayerStat.CREATOR);
        in.readTypedList(awayPlayers, PlayerStat.CREATOR);
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
        return homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
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
        return homePlayers;
    }

    public void setHomePlayers(List<PlayerStat> homePlayers) {
        this.homePlayers = homePlayers;
    }

    public List<PlayerStat> getAwayPlayers() {
        return awayPlayers;
    }

    public void setAwayPlayers(List<PlayerStat> awayPlayers) {
        this.awayPlayers = awayPlayers;
    }
}
