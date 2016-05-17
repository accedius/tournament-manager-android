package fit.cvut.org.cz.squash.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private boolean test = false;
    public void setTest(boolean test) {this.test = test;}

    public SQLiteDatabase getDatabase(Context context) {
        return new SquashDBHelper(context, test).getWritableDatabase();
    }

}
