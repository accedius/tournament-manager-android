package fit.cvut.org.cz.squash.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class SquashDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "TMSquash";
    private static final int DBVersion = 1;

    public SquashDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);
        db.execSQL(DBScripts.CREATE_TABLE_TOURNAMENTS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_COMPETITION);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TOURNAMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tCOMPETITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tTOURNAMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_COMPETITION);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_TOURNAMENT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
