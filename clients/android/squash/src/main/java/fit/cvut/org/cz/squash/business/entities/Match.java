package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

/**
 * Created by kevin on 9.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.business.entities.Match implements Parcelable {
    @DatabaseField(columnName = SDBConstants.cSETS_NUMBER)
    private int setsNumber;

    private String homeName;
    private String awayName;

    public Match() {}
    public Match(long id, long tournamentId, CompetitionType type, Date date, boolean played, String note, int period, int round, int setsNumber, int homeWonSets, int awayWonSets) {
        super(id, tournamentId, type, date, played, note, period, round);
        this.setsNumber = setsNumber;
    }

    public Match(fit.cvut.org.cz.tmlibrary.business.entities.Match m) {
        super(m);
        setsNumber = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(setsNumber);
        dest.writeString(homeName);
        dest.writeString(awayName);
    }

    public Match(Parcel in) {
        super(in);
        this.setsNumber = in.readInt();
        this.homeName = in.readString();
        this.awayName = in.readString();
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

    public int getSetsNumber() {
        return setsNumber;
    }

    public void setSetsNumber(int setsNumber) {
        this.setsNumber = setsNumber;
    }

    @Override
    public String getHomeName() {
        return homeName;
    }

    @Override
    public String getAwayName() {
        return awayName;
    }


    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public int getHomeWonSets() {
        return homeScore;
    }

    public void setHomeWonSets(int homeWonSets) {
        this.homeScore = homeWonSets;
    }

    public int getAwayWonSets() {
        return awayScore;
    }

    public void setAwayWonSets(int awayWonSets) {
        this.awayScore = awayWonSets;
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
}
