package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class HockeyDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "TMHockey.db";
    private static final int DBVersion = 10;

    public HockeyDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H1);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tCOMPETITIONS);
        db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H1);
        db.execSQL(DBScripts.INSERT_INTO_COMPETITIONS_H2);

    }


}
