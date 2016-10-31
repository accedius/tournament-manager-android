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

    public Setting(String packageName, String sportName) {
        this.packageName = packageName;
        this.sportName = sportName;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Setting))
            return false;

        Setting s = (Setting)o;
        return this.sportName.equals(s.sportName) && this.packageName.equals(s.packageName);
    }
}
