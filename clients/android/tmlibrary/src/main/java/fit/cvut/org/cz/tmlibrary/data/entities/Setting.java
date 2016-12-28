package fit.cvut.org.cz.tmlibrary.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

/**
 * Created by kevin on 31.10.2016.
 */
@DatabaseTable(tableName = DBConstants.tSETTINGS)
public class Setting implements IEntity {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private Long id;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cPACKAGE_NAME)
    private String packageName;

    @DatabaseField(canBeNull = false, columnName = DBConstants.cSPORT_NAME)
    private String sportName;

    // Necessary for ORMLite
    public Setting() {}

    public Setting(String packageName, String sportName) {
        this.packageName = packageName;
        this.sportName = sportName;
    }

    public long getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

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
