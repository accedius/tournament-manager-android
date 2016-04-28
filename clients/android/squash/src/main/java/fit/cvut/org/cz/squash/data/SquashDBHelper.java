package fit.cvut.org.cz.squash.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class SquashDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "TMSquash";
    private static final int DBVersion = 5;

    public SquashDBHelper(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);
        db.execSQL(DBScripts.CREATE_TABLE_TOURNAMENTS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_COMPETITION);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TOURNAMENT);
        db.execSQL(DBScripts.CREATE_TABLE_TEAMS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TEAM);
        db.execSQL(SDBScripts.CREATE_TABLE_POINT_CONFIG);
        db.execSQL(DBScripts.CREATE_TABLE_MATCHES);
        db.execSQL(DBScripts.CREATE_TABLE_PARTICIPANTS);
        db.execSQL(SDBScripts.CREATE_TABLE_STATS_ENUM);
        db.execSQL(SDBScripts.CREATE_TABLE_STATS);

        ContentValues cv = new ContentValues();
        cv.put(SDBConstants.cTYPE, StatsEnum.MATCH.toString());
        db.insert(SDBConstants.tSTATS_ENUM, null, cv);
        cv.clear();
        cv.put(SDBConstants.cTYPE, StatsEnum.SET.toString());
        db.insert(SDBConstants.tSTATS_ENUM, null,  cv);
        cv.clear();
        cv.put(SDBConstants.cTYPE, StatsEnum.MATCH_PARTICIPATION.toString());
        db.insert(SDBConstants.tSTATS_ENUM, null,  cv);
        cv.clear();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tCOMPETITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tTOURNAMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_COMPETITION);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_TOURNAMENT);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tTEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + SDBConstants.tPOINT_CONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tMATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + SDBConstants.tSTATS);
        db.execSQL("DROP TABLE IF EXISTS " + SDBConstants.tSTATS_ENUM);
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
