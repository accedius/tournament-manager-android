package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.business.entities.Match implements Parcelable {
    @DatabaseField(columnName = HockeyDBConstants.cSHOOTOUTS)
    private boolean shootouts;

    @DatabaseField(columnName = HockeyDBConstants.cOVERTIME)
    private boolean overtime;

    private String homeName;
    private String awayName;
    private int homeScore;
    private int awayScore;

    public Match() {}
    public Match(long id, long tournamentId, CompetitionType type, Date date, boolean played, String note, int period, int round, boolean shootouts, boolean overtime) {
        super(id, tournamentId, type, date, played, note, period, round);
        this.shootouts = shootouts;
        this.overtime = overtime;
    }

    public Match(fit.cvut.org.cz.tmlibrary.business.entities.Match m) {
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
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
    }

    public Match(Parcel in) {
        super(in);
        this.shootouts = in.readByte() != 0;
        this.overtime = in.readByte() != 0;
        this.homeScore = in.readInt();
        this.awayScore = in.readInt();
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

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}
