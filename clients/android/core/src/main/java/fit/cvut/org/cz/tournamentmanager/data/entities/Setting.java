package fit.cvut.org.cz.tournamentmanager.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Setting entity.
 */
@DatabaseTable(tableName = DBConstants.tSETTINGS)
public class Setting implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cPACKAGE_NAME)
    private String packageName;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cSPORT_NAME)
    private String sportName;

    public Setting() {}

    /**
     * Setting constructor.
     * @param packageName name of package
     * @param sportName name of sport
     */
    public Setting(String packageName, String sportName) {
        this.packageName = packageName;
        this.sportName = sportName;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Package Name getter.
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sport Name getter.
     * @return sportName
     */
    public String getSportName() {
        return sportName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Setting))
            return false;

        Setting s = (Setting)o;
        return this.sportName.equals(s.sportName) && this.packageName.equals(s.packageName);
    }
}
