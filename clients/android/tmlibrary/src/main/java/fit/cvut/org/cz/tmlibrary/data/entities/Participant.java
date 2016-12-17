package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tPARTICIPANTS)
public class Participant implements IEntity, Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cMATCH_ID)
    private long matchId;

    // Team id or Player id (based on Competition type)
    @DatabaseField(columnName = DBConstants.cPARTICIPANT_ID)
    private long participantId;

    @DatabaseField(columnName = DBConstants.cROLE)
    private String role;

    private String name;

    private List<? extends ParticipantStat> participantStats;
    private List<? extends PlayerStat> playerStats;

    public Participant() {}

    public Participant(Participant p) {
        this.matchId = p.matchId;
        this.participantId = p.participantId;
        this.role = p.role;
        this.name = p.name;
    }

    public Participant(long matchId, long participantId, String role) {
        this.matchId = matchId;
        this.participantId = participantId;
        this.role = role;
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    protected Participant(Parcel in) {
        id = in.readLong();
        matchId = in.readLong();
        participantId = in.readLong();
        role = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(matchId);
        dest.writeLong(participantId);
        dest.writeString(role);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public long getId() {
        return id;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long match_id) {
        this.matchId = match_id;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participant_id) {
        this.participantId = participant_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<? extends ParticipantStat> getParticipantStats() {
        return participantStats;
    }

    public void setParticipantStats(List<? extends ParticipantStat> participantStats) {
        this.participantStats = participantStats;
    }

    public List<? extends PlayerStat> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(List<? extends PlayerStat> playerStats) {
        this.playerStats = playerStats;
    }
}
