package fit.cvut.org.cz.tournamentmanager.business.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

/**
 * Created by kevin on 19.10.2016.
 */
public class JsonFileValidator {
    public static boolean valid(String fileContent, String sportContext) {
        try {
            ServerCommunicationItem item = new Gson().fromJson(fileContent, ServerCommunicationItem.class);
            String fileSportContext = String.valueOf(item.syncData.get("sport_context"));
            return sportContext.equals(fileSportContext);
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}

