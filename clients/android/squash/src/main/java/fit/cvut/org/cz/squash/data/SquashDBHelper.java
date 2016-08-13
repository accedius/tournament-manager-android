package fit.cvut.org.cz.squash.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Helper class that is responsible for creating and managing apllication database and allows us acsess to it
 * Created by Vaclav on 25. 3. 2016.
 */
public class SquashDBHelper extends SQLiteOpenHelper {

    private static final String DBName = "TMSquash";
    private static final int DBVersion = 7;
    private boolean test = false;

    public SquashDBHelper(Context context, boolean test) {
        super(context, DBName, null, DBVersion);
        this.test = test;
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

        db.execSQL(DBScripts.INSERT_SQUASH_COMPETITIONS);
        db.execSQL(DBScripts.INSERT_SQUASH_COMPETITIONS_1);
        db.execSQL(DBScripts.INSERT_SQUASH_COMPETITIONS_2);

        db.execSQL(DBScripts.INSERT_SQUASH_TOURNAMENTS);
        db.execSQL(DBScripts.INSERT_SQUASH_TOURNAMENTS_1);
        db.execSQL(DBScripts.INSERT_SQUASH_TOURNAMENTS_2);

        db.execSQL(SDBScripts.INSERT_POINT_CONFIG);
        db.execSQL(SDBScripts.INSERT_POINT_CONFIG_1);
        db.execSQL(SDBScripts.INSERT_POINT_CONFIG_2);

        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_1);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_2);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_3);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_4);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_5);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_6);
        db.execSQL(DBScripts.INSERT_PLAYER_COMPETITION_7);

        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_1);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_2);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_3);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_4);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_5);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_6);
        db.execSQL(DBScripts.INSERT_PLAYER_TOURNAMENT_7);

        db.execSQL(DBScripts.INSERT_TOURNAMENT_TEAMS);
        db.execSQL(DBScripts.INSERT_TOURNAMENT_TEAMS_1);
        db.execSQL(DBScripts.INSERT_TOURNAMENT_TEAMS_2);
        db.execSQL(DBScripts.INSERT_TOURNAMENT_TEAMS_3);

        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_1);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_2);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_3);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_4);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_5);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_6);
        db.execSQL(DBScripts.INSERT_PLAYER_TEAMS_7);

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
        if (!db.isReadOnly() && !test) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
