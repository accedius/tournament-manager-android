package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tPARTICIPANTS)
public class Participant implements IEntity, Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cMATCH_ID)
    private long match_id;

    // Team id or Player id (based on Competition type)
    @DatabaseField(columnName = DBConstants.cPARTICIPANT_ID)
    private long participant_id;

    @DatabaseField(columnName = DBConstants.cROLE)
    private String role;

    private String name;

    private List<? extends ParticipantStat> participantStats;
    private List<? extends PlayerStat> playerStats;

    public Participant() {}

    public Participant(Participant p) {
        this.match_id = p.match_id;
        this.participant_id = p.participant_id;
        this.role = p.role;
        this.name = p.name;
    }

    public Participant(long match_id, long participant_id, String role) {
        this.match_id = match_id;
        this.participant_id = participant_id;
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
        match_id = in.readLong();
        participant_id = in.readLong();
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
        dest.writeLong(match_id);
        dest.writeLong(participant_id);
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
        return match_id;
    }

    public void setMatchId(long match_id) {
        this.match_id = match_id;
    }

    public long getParticipantId() {
        return participant_id;
    }

    public void setParticipantId(long participant_id) {
        this.participant_id = participant_id;
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
