package fit.cvut.org.cz.tournamentmanager.business.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

/**
 * Validator used for check if given JSON string is valid ServerCommunicationItem or not.
 */
public class JsonFileValidator {
    /**
     * Method to check if JSON content for given sport is valid ServerCommunicationItem.
     * @param fileContent content of file
     * @param sportContext given sport
     * @return True if content is valid, false otherwise.
     */
    public static boolean valid(String fileContent, String sportContext) {
        try {
            JsonReader fileContentJsonReader = new JsonReader(new StringReader(fileContent.trim()));
            fileContentJsonReader.setLenient(true);;
            ServerCommunicationItem item = new Gson().fromJson(fileContentJsonReader, ServerCommunicationItem.class);
            String fileSportContext = String.valueOf(item.syncData.get(Constants.SPORT));
            return sportContext.equals(fileSportContext);
        } catch (JsonSyntaxException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }
}

