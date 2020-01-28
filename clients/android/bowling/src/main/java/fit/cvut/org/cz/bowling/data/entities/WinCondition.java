package fit.cvut.org.cz.bowling.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

@DatabaseTable(tableName = DBConstants.tWIN_CONDITION)
public class WinCondition implements IEntity {
    @DatabaseField(columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cTOURNAMENT_ID)
    private long tournamentId;

    @DatabaseField(columnName = DBConstants.cWIN_CONDITION)
    private int winCondition;

    @Override
    public long getId() {
        return tournamentId;
    }
}
