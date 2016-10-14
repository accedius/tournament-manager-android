package fit.cvut.org.cz.tmlibrary.business.serialization;

/**
 * Created by kevin on 7.10.2016.
 */
public class ServerPackageResource {
    public String name;
    public String displayName;
    public String detailUrl;
    public String platform = "android";

    public ServerPackageResource(String name, String displayName, String detailUrl) {
        this.name = name;
        this.displayName = displayName;
        this.detailUrl = detailUrl;
    }
}
