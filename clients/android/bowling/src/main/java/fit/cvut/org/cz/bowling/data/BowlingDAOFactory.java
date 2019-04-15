package fit.cvut.org.cz.bowling.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;

public class BowlingDAOFactory extends DAOFactory implements IDAOFactory {
    private static final int DBVersion = 1;

    public BowlingDAOFactory(Context context, String name) {
        super(context, name, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Competition.class);
            TableUtils.createTable(connectionSource, CompetitionPlayer.class);
            TableUtils.createTable(connectionSource, Tournament.class);
            TableUtils.createTable(connectionSource, TournamentPlayer.class);
            //TableUtils.createTable(connectionSource, PointConfiguration.class);
            TableUtils.createTable(connectionSource, Team.class);
            TableUtils.createTable(connectionSource, TeamPlayer.class);
            //TableUtils.createTable(connectionSource, Match.class);
            TableUtils.createTable(connectionSource, Participant.class);
            //TableUtils.createTable(connectionSource, ParticipantStat.class);
            TableUtils.createTable(connectionSource, PlayerStat.class);
        } catch (SQLException e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Competition.class, true);
            TableUtils.dropTable(connectionSource, CompetitionPlayer.class, true);
            TableUtils.dropTable(connectionSource, Tournament.class, true);
            TableUtils.dropTable(connectionSource, TournamentPlayer.class, true);
            //TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            TableUtils.dropTable(connectionSource, Team.class, true);
            TableUtils.dropTable(connectionSource, TeamPlayer.class, true);
            //TableUtils.dropTable(connectionSource, Match.class, true);
            TableUtils.dropTable(connectionSource, Participant.class, true);
            //TableUtils.dropTable(connectionSource, ParticipantStat.class, true);
            TableUtils.dropTable(connectionSource, PlayerStat.class, true);
        } catch (SQLException e) {}
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}