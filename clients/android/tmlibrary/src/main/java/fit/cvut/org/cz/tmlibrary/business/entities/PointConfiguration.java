package fit.cvut.org.cz.tmlibrary.business.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 4.12.2016.
 */
@DatabaseTable(tableName = DBConstants.tCONFIGURATIONS)
public class PointConfiguration implements IEntity {
    @DatabaseField(id = true, columnName = DBConstants.cTOURNAMENT_ID)
    public long tournamentId;

    @Override
    public long getId() {
        return tournamentId;
    }
}
