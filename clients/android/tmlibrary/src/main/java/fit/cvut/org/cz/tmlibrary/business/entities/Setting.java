package fit.cvut.org.cz.tmlibrary.business.entities;

import fit.cvut.org.cz.tmlibrary.data.entities.DSetting;

/**
 * Created by kevin on 31.10.2016.
 */
public class Setting {
    private String packageName;
    private String sportName;

    public Setting(DSetting dSetting) {
        packageName = dSetting.getPackageName();
        sportName = dSetting.getSportName();
    }

    public static DSetting convertToDSetting(Setting s) {
        return new DSetting(s.packageName, s.sportName);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSportName() {
        return sportName;
    }
}
