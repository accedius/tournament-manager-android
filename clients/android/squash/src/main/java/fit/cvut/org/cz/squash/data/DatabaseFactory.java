package fit.cvut.org.cz.squash.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public class DatabaseFactory {

    private static DatabaseFactory ourInstance = new DatabaseFactory();

    public static DatabaseFactory getInstance() {
        return ourInstance;
    }

    private DatabaseFactory() {
    }

    public SQLiteDatabase getDatabase(Context context) { return new SquashDBHelper(getA)}
}
