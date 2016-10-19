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
    private static final int DBVersion = 1;

    public HockeyDBHelper(Context context, String name) {
        super(context, name+".db", null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);

        db.execSQL(DBScripts.CREATE_TABLE_TOURNAMENTS);

        db.execSQL(HockeyDBScripts.CREATE_TABLE_CONFIGURATIONS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_COMPETITION);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TOURNAMENT);
        db.execSQL(DBScripts.CREATE_TABLE_MATCHES);
        db.execSQL(DBScripts.CREATE_TABLE_PARTICIPANTS);
        db.execSQL(DBScripts.CREATE_TABLE_STATS);
        db.execSQL(DBScripts.CREATE_TABLE_TEAMS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TEAM);
        db.execSQL(HockeyDBScripts.CREATE_TABLE_MATCH_SCORE);

        db.execSQL(DBScripts.INSERT_HOCKEY_COMPETITIONS);
        db.execSQL(DBScripts.INSERT_HOCKEY_COMPETITIONS_1);
        db.execSQL(DBScripts.INSERT_HOCKEY_COMPETITIONS_2);

        db.execSQL(DBScripts.INSERT_HOCKEY_TOURNAMENTS);
        db.execSQL(DBScripts.INSERT_HOCKEY_TOURNAMENTS_1);
        db.execSQL(DBScripts.INSERT_HOCKEY_TOURNAMENTS_2);

        db.execSQL(HockeyDBScripts.INSERT_POINT_CONFIG);
        db.execSQL(HockeyDBScripts.INSERT_POINT_CONFIG_1);
        db.execSQL(HockeyDBScripts.INSERT_POINT_CONFIG_2);

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tCOMPETITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tTOURNAMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + HockeyDBConstants.tCONFIGURATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_COMPETITION);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_TOURNAMENT);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tTEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tMATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tSTATS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.tPLAYERS_IN_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + HockeyDBConstants.tMATCH_SCORE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
