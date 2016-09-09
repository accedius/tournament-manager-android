package fit.cvut.org.cz.tournamentmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class CoreDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "TMCore";
    private static final int DBVersion = 1;

    public CoreDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_1);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_2);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_3);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_4);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_5);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_6);
        db.execSQL(DBScripts.INSERT_INTO_PLAYERS_7);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
