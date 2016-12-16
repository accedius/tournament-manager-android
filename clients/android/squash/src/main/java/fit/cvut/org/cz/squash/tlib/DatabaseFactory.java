package fit.cvut.org.cz.squash.tlib;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.squash.tlib.SquashHelper2;

/**
 * This class allows acess to application database
 * Created by Vaclav on 29. 3. 2016.
 */
public class DatabaseFactory {
    private static HashMap<String, SquashHelper2> dbHelpers = new HashMap<>();

    public static SquashHelper2 getDBeHelper(Context context) {
        String name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        if (!dbHelpers.containsKey(name)) {
            dbHelpers.put(name, new SquashHelper2(context, name));
        }
        return dbHelpers.get(name);
    }
}
