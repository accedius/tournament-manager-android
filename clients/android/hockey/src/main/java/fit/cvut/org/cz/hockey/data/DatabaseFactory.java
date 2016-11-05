package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import fit.cvut.org.cz.hockey.presentation.HockeyPackage;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class DatabaseFactory {
    private static HashMap<String, HockeyDBHelper> dbHelpers = new HashMap<>();

    public static SQLiteDatabase getDatabase(Context context) {
        String name = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        if (!dbHelpers.containsKey(name)) {
            dbHelpers.put(name, new HockeyDBHelper(context, name));
        }
        return dbHelpers.get(name).getWritableDatabase();
    }

    public static HockeyDBHelper getDBeHelper(Context context) {
        String name = ((HockeyPackage) context.getApplicationContext()).getSportContext();
        if (!dbHelpers.containsKey(name)) {
            dbHelpers.put(name, new HockeyDBHelper(context, name));
        }
        return dbHelpers.get(name);
    }
}
