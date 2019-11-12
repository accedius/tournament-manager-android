package fit.cvut.org.cz.bowling.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;

public class BowlingDAOFactory extends DAOFactory implements IDAOFactory {
    private static final int DBVersion = 5;

    public BowlingDAOFactory(Context context, String name) {
        super(context, name, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Competition.class);
            TableUtils.createTableIfNotExists(connectionSource, CompetitionPlayer.class);
            TableUtils.createTableIfNotExists(connectionSource, Tournament.class);
            TableUtils.createTableIfNotExists(connectionSource, TournamentPlayer.class);
            TableUtils.createTableIfNotExists(connectionSource, PointConfiguration.class);
            TableUtils.createTableIfNotExists(connectionSource, Team.class);
            TableUtils.createTableIfNotExists(connectionSource, TeamPlayer.class);
            TableUtils.createTableIfNotExists(connectionSource, Match.class);
            TableUtils.createTableIfNotExists(connectionSource, Participant.class);
            TableUtils.createTableIfNotExists(connectionSource, ParticipantStat.class);
            TableUtils.createTableIfNotExists(connectionSource, PlayerStat.class);
        } catch (SQLException e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int fromVersion = oldVersion;

        /*Upgrade from versions <=3 to version 4*/
        if(fromVersion <= 3) {
            try {
                TableUtils.dropTable(connectionSource, Match.class, true);
                TableUtils.dropTable(connectionSource, PlayerStat.class, true);
                TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            } catch (SQLException e) {}
            onCreate(db, connectionSource);
            fromVersion = 4;
        }
        switch (fromVersion) {
            case 4: /*from 4 to 5*/
                fromVersion++;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int fromVersion = oldVersion; fromVersion > newVersion; --fromVersion){
            switch (fromVersion) {
                case 5: /*from 5 to 4*/
                    break;
                case 4: /*from 4 to 3 (and less)*/
                    try {
                        TableUtils.dropTable(connectionSource, Match.class, true);
                        TableUtils.dropTable(connectionSource, PlayerStat.class, true);
                        TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
                    } catch (SQLException e) {}
                    onCreate(db, connectionSource);
                    fromVersion = 1;
                    break;
            }
        }
    }
}