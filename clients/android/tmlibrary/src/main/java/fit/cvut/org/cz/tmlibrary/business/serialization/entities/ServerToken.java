package fit.cvut.org.cz.tmlibrary.business.serialization.entities;

/**
 * Created by kevin on 7.10.2016.
 */
public class ServerToken {
    String value;
    ServerTokenType type;

    public ServerToken(ServerTokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}
