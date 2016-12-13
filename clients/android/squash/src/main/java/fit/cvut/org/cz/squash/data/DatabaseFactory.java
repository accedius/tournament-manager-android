package fit.cvut.org.cz.squash.data;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.squash.presentation.SquashPackage;

/**
 * This class allows acess to application database
 * Created by Vaclav on 29. 3. 2016.
 */
public class DatabaseFactory {
    private static HashMap<String, SquashDBHelper> dbHelpers = new HashMap<>();

    public static SquashDBHelper getDBeHelper(Context context) {
        String name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        if (!dbHelpers.containsKey(name)) {
            dbHelpers.put(name, new SquashDBHelper(context, name));
        }
        return dbHelpers.get(name);
    }
}
