package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kevin on 7. 4. 2016.
 */
public class DatabaseFactory {
    private static DatabaseFactory ourInstance = new DatabaseFactory();

    public static DatabaseFactory getInstance() {
        return ourInstance;
    }

    private DatabaseFactory() {
    }

    public SQLiteDatabase getDatabase(Context context) { return new CoreDBHelper(context).getWritableDatabase();}
}
