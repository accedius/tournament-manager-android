package fit.cvut.org.cz.tmlibrary.tlib;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 10.11.2016.
 */
@DatabaseTable(tableName = DBConstants.tPLAYERS_IN_TOURNAMENT)
public class TournamentPlayer implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cTOURNAMENT_ID, canBeNull = false, foreign = true)
    private Tournament tournament;

    @DatabaseField(columnName = DBConstants.cPLAYER_ID)
    private long playerId;

    public TournamentPlayer() {}

    public TournamentPlayer(Tournament tournament, long playerId) {
        this.tournament = tournament;
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
        return tournament.getId();
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
