package fit.cvut.org.cz.tmlibrary.data.entities;

/**
 * Created by kevin on 31.10.2016.
 */
public class DSetting {
    private String packageName;
    private String sportName;

    public DSetting(String packageName, String sportName) {
        this.packageName = packageName;
        this.sportName = sportName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSportName() {
        return sportName;
    }
}
