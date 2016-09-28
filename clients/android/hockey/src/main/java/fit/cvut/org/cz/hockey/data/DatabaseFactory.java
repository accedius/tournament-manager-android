package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by atgot_000 on 31. 3. 2016.
 */
public class DatabaseFactory {
    private static DatabaseFactory instance = new DatabaseFactory();

    public static DatabaseFactory getInstance() {
        return instance;
    }

    private DatabaseFactory() {
    }

    public SQLiteDatabase getDatabase(Context context) {
        HockeyDBHelper helper = new HockeyDBHelper(context);
        return helper.getWritableDatabase();
    }
}
