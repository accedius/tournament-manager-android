package fit.cvut.org.cz.squash.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import fit.cvut.org.cz.squash.presentation.SquashPackage;

/**
 * This class allows acess to application database
 * Created by Vaclav on 29. 3. 2016.
 */
public class DatabaseFactory {
    private static DatabaseFactory ourInstance = new DatabaseFactory();

    public static DatabaseFactory getInstance() {
        return ourInstance;
    }

    private DatabaseFactory() {
    }

    public SQLiteDatabase getDatabase(Context context) {
        String name = ((SquashPackage) context.getApplicationContext()).getSportContext();
        return new SquashDBHelper(context, name).getWritableDatabase();
    }
}
