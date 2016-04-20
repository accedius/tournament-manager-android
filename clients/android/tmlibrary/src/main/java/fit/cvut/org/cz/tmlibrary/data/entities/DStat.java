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
