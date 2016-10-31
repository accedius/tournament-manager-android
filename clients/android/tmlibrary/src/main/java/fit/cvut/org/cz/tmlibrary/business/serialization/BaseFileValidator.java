package fit.cvut.org.cz.tmlibrary.business.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by kevin on 19.10.2016.
 */
public class BaseFileValidator {
    public static boolean validJsonFile(String fileContent, String sportContext) {
        try {
            ServerCommunicationItem item = new Gson().fromJson(fileContent, ServerCommunicationItem.class);
            return item.syncData.get("sport_context").equals(sportContext);
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}

