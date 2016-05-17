package fit.cvut.org.cz.squash.data.entities;

/**
 * Represent statistic on data layer
 * Created by Vaclav on 21. 4. 2016.
 */
public class DStat {

    private long id, competitionId, tournamentId, playerId, participantId;
    private int status, lostValue, value;
    private StatsEnum type;

    public DStat() {}

    public DStat(long id, long competitionId, long tournamentId, long playerId, long participantId, int status, int lostValue, int value, StatsEnum type) {
        this.id = id;
        this.competitionId = competitionId;
        this.tournamentId = tournamentId;
        this.playerId = playerId;
        this.participantId = participantId;
        this.status = status;
        this.lostValue = lostValue;
        this.value = value;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(long competitionId) {
        this.competitionId = competitionId;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * @return id or -1 if null in DB
     */
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

    /**
     *
     * @return if type is MATCH 1 for win 0 for draw -1 for loss, if type is SET 1 for win -1 for loss
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Only valid if type is SET
     * @return value or -1 if null
     */
    public int getLostValue() {
        return lostValue;
    }

    public void setLostValue(int lostValue) {
        this.lostValue = lostValue;
    }

    /**
     * Only valid if type is SET
     * @return value or -1 if null
     */
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public StatsEnum getType() {
        return type;
    }

    public void setType(StatsEnum type) {
        this.type = type;
    }
}
