package fit.cvut.org.cz.tmlibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by kevin on 5.11.2016.
 */
abstract public class SportDBHelper extends OrmLiteSqliteOpenHelper {
    private Dao<Competition, Long> competitionDAO;
    private Dao<Tournament, Long> tournamentDAO;
    private Dao<Team, Long> teamDAO;

    public SportDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public Dao<Competition, Long> getCompetitionDao() {
        if (competitionDAO == null) {
            try {
                competitionDAO = getDao(Competition.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return competitionDAO;
    }

    public Dao<Tournament, Long> getTournamentDao() {
        if (tournamentDAO == null) {
            try {
                tournamentDAO = getDao(Tournament.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return tournamentDAO;
    }

    public Dao<Team, Long> getTeamDao() {
        if (teamDAO == null) {
            try {
                teamDAO = getDao(Team.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return teamDAO;
    }
}
