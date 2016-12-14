package fit.cvut.org.cz.tmlibrary.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 10.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYERS_IN_TEAM)
public class TeamPlayer implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cTEAM_ID)
    private long teamId;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    public TeamPlayer() {}

    public TeamPlayer(long teamId, long playerId) {
        this.teamId = teamId;
        this.playerId = playerId;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
