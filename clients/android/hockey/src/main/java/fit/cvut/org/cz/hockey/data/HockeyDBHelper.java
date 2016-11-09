package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
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
    private Dao<Tournament, Long> tournamentDao;
    private String DBName;
    private Dao<PointConfiguration, Long> pointConfigurationDao;

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

    public Dao<Tournament, Long> getTournamentDao() {
        if (tournamentDao == null) {
            try {
                tournamentDao = getDao(Tournament.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return tournamentDao;
    }

    public Dao<PointConfiguration, Long> getPointConfigurationDao() {
        if (pointConfigurationDao == null) {
            try {
                pointConfigurationDao = getDao(PointConfiguration.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return pointConfigurationDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Competition.class);
            TableUtils.createTable(connectionSource, Tournament.class);
        } catch (SQLException e) {}

        db.execSQL(HockeyDBScripts.CREATE_TABLE_CONFIGURATIONS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_COMPETITION);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TOURNAMENT);
        db.execSQL(DBScripts.CREATE_TABLE_MATCHES);
        db.execSQL(DBScripts.CREATE_TABLE_PARTICIPANTS);
        db.execSQL(DBScripts.CREATE_TABLE_STATS);
        db.execSQL(DBScripts.CREATE_TABLE_TEAMS);
        db.execSQL(DBScripts.CREATE_TABLE_PLAYERS_IN_TEAM);
        db.execSQL(HockeyDBScripts.CREATE_TABLE_MATCH_SCORE);

        /* Create competitions. */
        ArrayList<Competition> competitionArrayList = new ArrayList<>();
        Date date1 = new Date();
        Date date2 = new Date();
        date1.setTime(1420074061000L);
        date2.setTime(1422666061000L);
        competitionArrayList.add(new Competition(1, "H CMP1", date1, date2, "Pozn 1", CompetitionTypes.teams()));
        competitionArrayList.add(new Competition(2, "H CMP2", date1, date2, "Pozn 2", CompetitionTypes.teams()));
        competitionArrayList.add(new Competition(3, "H CMP3", date1, date2, "Pozn 3", CompetitionTypes.teams()));

        for (Competition c : competitionArrayList) {
            c.setEtag("etag_"+getDatabaseName());
            c.setUid("UID_"+c.getName());
            c.setSportContext(DBName);
            c.setLastModified(new Date());
            c.setLastSynchronized(new Date());
            c.setTokenValue("token_value");
        }

        /* Create tournaments. */
        ArrayList<Tournament> tournamentArrayList = new ArrayList<>();
        tournamentArrayList.add(new Tournament(1, 1, "Tour 1 - CMP1", date1, date2, "Pozn Tour 11"));
        tournamentArrayList.add(new Tournament(2, 1, "Tour 2 - CMP1", date1, date2, "Pozn Tour 21"));
        tournamentArrayList.add(new Tournament(3, 2, "Tour 1 - CMP2", date1, date2, "Pozn Tour 12"));
        for (Tournament t : tournamentArrayList) {
            t.setEtag("etag_"+getDatabaseName());
            t.setUid("UID_"+t.getName());
            t.setLastModified(new Date());
            t.setLastSynchronized(new Date());
            t.setTokenValue("token_value");
        }

        /* Create point config for tournaments. */
        ArrayList<PointConfiguration> pointConfigurationArrayList = new ArrayList<>();
        pointConfigurationArrayList.add(new PointConfiguration(1, 3, 1, 0, 2, 1, 1, 2, 1));
        pointConfigurationArrayList.add(new PointConfiguration(2, 3, 1, 0, 2, 1, 1, 2, 1));
        pointConfigurationArrayList.add(new PointConfiguration(3, 3, 1, 0, 2, 1, 1, 2, 1));

        try {
            getCompetitionDao().create(competitionArrayList);
            getTournamentDao().create(tournamentArrayList);
            getPointConfigurationDao().create(pointConfigurationArrayList);
        } catch (SQLException e) {}

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
            TableUtils.dropTable(connectionSource, Tournament.class, true);
        } catch (SQLException e) {}
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
