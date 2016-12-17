package fit.cvut.org.cz.squash.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;

/**
 * Created by kevin on 9.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.data.entities.Match implements Parcelable {
    @DatabaseField(columnName = SDBConstants.cSETS_NUMBER)
    private int setsNumber;

    private String homeName;
    private String awayName;
    private List<SetRowItem> sets = new ArrayList<>();
    private List<PlayerStat> homePlayers = new ArrayList<>();
    private List<PlayerStat> awayPlayers = new ArrayList<>();

    public Match() {}

    public Match(fit.cvut.org.cz.tmlibrary.data.entities.Match m) {
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
        dest.writeTypedList(sets);
        dest.writeTypedList(homePlayers);
        dest.writeTypedList(awayPlayers);
    }

    public Match(Parcel in) {
        super(in);
        this.setsNumber = in.readInt();
        this.homeName = in.readString();
        this.awayName = in.readString();
        in.readTypedList(sets, SetRowItem.CREATOR);
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

    public List<SetRowItem> getSets() {
        return sets;
    }

    public void setSets(List<SetRowItem> sets) {
        this.sets = sets;
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
