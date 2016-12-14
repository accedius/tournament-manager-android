package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kevin on 7. 4. 2016.
 */
public class DatabaseFactory {
    public static CoreDBHelper getDBHelper(Context context) { return new CoreDBHelper(context);}
}
