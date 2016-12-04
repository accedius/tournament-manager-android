package fit.cvut.org.cz.tmlibrary.business.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 10.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYERS_IN_COMPETITION)
public class CompetitionPlayer implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cCOMPETITION_ID)
    private long competitionId;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    public CompetitionPlayer() {}

    public CompetitionPlayer(long competitionId, long playerId) {
        this.competitionId = competitionId;
        this.playerId = playerId;
    }

    @Override
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

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
