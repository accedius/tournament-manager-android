package fit.cvut.org.cz.tmlibrary.data.entities;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class DStat {
    private long id, playerId, participantId, statsEnumId, tournamentId, competitionId;
    private String value;

    public DStat() {}

    public DStat(long id, long playerId, long participantId, long statsEnumId, long tournamentId, long competitionId, String value) {
        this.id = id;
        this.playerId = playerId;
        this.participantId = participantId;
        this.statsEnumId = statsEnumId;
        this.tournamentId = tournamentId;
        this.competitionId = competitionId;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;

        if( !DStat.class.isAssignableFrom( o.getClass()) ) return false;

        final DStat other = (DStat) o;
        if( (this.value == null) ? (other.value != null) : !this.value.equals(other.getValue()) ) return false;

        if( this.id != other.getId() || this.playerId != other.getPlayerId() || this.participantId != other.getParticipantId()
                || this.statsEnumId != other.getStatsEnumId() || this.tournamentId != other.getTournamentId()
                || this.competitionId != other.getCompetitionId()) return false;
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public long getStatsEnumId() {
        return statsEnumId;
    }

    public void setStatsEnumId(long statsEnumId) {
        this.statsEnumId = statsEnumId;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(long competitionId) {
        this.competitionId = competitionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
