package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class HockeyDAOFactory extends DAOFactory {
    private static final int DBVersion = 1;

    public HockeyDAOFactory(Context context, String name) {
        super(context, name, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Competition.class);
            TableUtils.createTable(connectionSource, CompetitionPlayer.class);
            TableUtils.createTable(connectionSource, Tournament.class);
            TableUtils.createTable(connectionSource, TournamentPlayer.class);
            TableUtils.createTable(connectionSource, PointConfiguration.class);
            TableUtils.createTable(connectionSource, Team.class);
            TableUtils.createTable(connectionSource, TeamPlayer.class);
            TableUtils.createTable(connectionSource, Match.class);
            TableUtils.createTable(connectionSource, Participant.class);
            TableUtils.createTable(connectionSource, ParticipantStat.class);
            TableUtils.createTable(connectionSource, PlayerStat.class);
        } catch (SQLException e) {}

//        /* Create competitions. */
//        ArrayList<Competition> competitionArrayList = new ArrayList<>();
//        Date date1 = new Date();
//        Date date2 = new Date();
//        date1.setTime(1420074061000L);
//        date2.setTime(1422666061000L);
//        competitionArrayList.add(new Competition(1, "H CMP1", date1, date2, "Pozn 1", CompetitionTypes.teams()));
//        competitionArrayList.add(new Competition(2, "H CMP2", date1, date2, "Pozn 2", CompetitionTypes.teams()));
//        competitionArrayList.add(new Competition(3, "H CMP3", date1, date2, "Pozn 3", CompetitionTypes.teams()));
//
//        for (Competition c : competitionArrayList) {
//            c.setEtag("etag_"+getDatabaseName());
//            c.setUid("UID_"+c.getName());
//            c.setSportContext(DBName);
//            c.setLastModified(new Date());
//            c.setLastSynchronized(new Date());
//            c.setTokenValue("token_value");
//        }
//
//        /* Create tournaments. */
//        ArrayList<Tournament> tournamentArrayList = new ArrayList<>();
//        tournamentArrayList.add(new Tournament(1, 1, "Tour 1 - CMP1", date1, date2, "Pozn Tour 11"));
//        tournamentArrayList.add(new Tournament(2, 1, "Tour 2 - CMP1", date1, date2, "Pozn Tour 21"));
//        tournamentArrayList.add(new Tournament(3, 2, "Tour 1 - CMP2", date1, date2, "Pozn Tour 12"));
//        for (Tournament t : tournamentArrayList) {
//            t.setEtag("etag_"+getDatabaseName());
//            t.setUid("UID_"+t.getName());
//            t.setLastModified(new Date());
//            t.setLastSynchronized(new Date());
//            t.setTokenValue("token_value");
//        }
//
//        /* Create point config for tournaments. */
//        ArrayList<PointConfiguration> pointConfigurationArrayList = new ArrayList<>();
//        pointConfigurationArrayList.add(new PointConfiguration(1, 3, 1, 0, 2, 1, 1, 2, 1));
//        pointConfigurationArrayList.add(new PointConfiguration(2, 3, 1, 0, 2, 1, 1, 2, 1));
//        pointConfigurationArrayList.add(new PointConfiguration(3, 3, 1, 0, 2, 1, 1, 2, 1));
//
//        /* Create teams. */
//        ArrayList<Team> teamArrayList = new ArrayList<>();
//        teamArrayList.add(new Team(1, 1, "A team"));
//        teamArrayList.add(new Team(2, 1, "B team"));
//        teamArrayList.add(new Team(3, 1, "C team"));
//        teamArrayList.add(new Team(4, 1, "D team"));
//        for (Team t : teamArrayList) {
//            t.setEtag("etag_"+getDatabaseName());
//            t.setUid("UID_"+t.getName());
//            t.setLastModified(new Date());
//            t.setLastSynchronized(new Date());
//            t.setTokenValue("token_value");
//        }
//
//        try {
//            getCompetitionDAO().create(competitionArrayList);
//            getTournamentDAO().create(tournamentArrayList);
//            getHockeyPointConfigurationDAO().create(pointConfigurationArrayList);
//            getTeamDAO().create(teamArrayList);
//        } catch (SQLException e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Competition.class, true);
            TableUtils.dropTable(connectionSource, CompetitionPlayer.class, true);
            TableUtils.dropTable(connectionSource, Tournament.class, true);
            TableUtils.dropTable(connectionSource, TournamentPlayer.class, true);
            TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            TableUtils.dropTable(connectionSource, Team.class, true);
            TableUtils.dropTable(connectionSource, TeamPlayer.class, true);
            TableUtils.dropTable(connectionSource, Match.class, true);
            TableUtils.dropTable(connectionSource, Participant.class, true);
            TableUtils.dropTable(connectionSource, ParticipantStat.class, true);
            TableUtils.dropTable(connectionSource, PlayerStat.class, true);
        } catch (SQLException e) {}
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
