package fit.cvut.org.cz.tmlibrary.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 10.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYERS_IN_TOURNAMENT)
public class TournamentPlayer implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cTOURNAMENT_ID)
    private long tournamentId;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    public TournamentPlayer() {}

    public TournamentPlayer(long tournamentId, long playerId) {
        this.tournamentId = tournamentId;
        this.playerId = playerId;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
