package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class HockeyDBHelper extends SportDBHelper {
    private static final int DBVersion = 1;
    private Dao<Competition, Long> competitionDao;
    private String DBName;

    public HockeyDBHelper(Context context, String name) {
        super(context, name+".db", null, DBVersion);
        DBName = name;
    }

    public Dao<Competition, Long> getCompetitionDao() {
        if (competitionDao == null) {
            try {
                competitionDao = getDao(Competition.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return competitionDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Competition.class);
        } catch (SQLException e) {}
        //db.execSQL(DBScripts.CREATE_TABLE_COMPETITIONS);
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

        ArrayList<Competition> competitionArrayList = new ArrayList<>();
        Date date1 = new Date();
        Date date2 = new Date();
        date1.setTime(1420074061000L);
        date2.setTime(1422666061000L);
        competitionArrayList.add(new Competition(1, "H CMP1", date1, date2, "Pozn 1", CompetitionTypes.teams()));
        competitionArrayList.add(new Competition(3, "H CMP3", date1, date2, "Pozn 3", CompetitionTypes.teams()));
        date1.setTime(1451610061000L);
        date2.setTime(1454202061000L);
        competitionArrayList.add(new Competition(2, "H CMP2", date1, date2, "Pozn 2", CompetitionTypes.teams()));

        for (Competition c : competitionArrayList) {
            c.setEtag("etag_"+getDatabaseName());
            c.setUid("UID_"+c.getName());
            c.setSportContext(DBName);
            c.setLastModified(new Date());
            c.setLastSynchronized(new Date());
            c.setTokenValue("token_value");
        }

        try {
            getCompetitionDao().create(competitionArrayList);
        } catch (SQLException e) {}

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
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Competition.class, true);
        } catch (SQLException e) {}
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
