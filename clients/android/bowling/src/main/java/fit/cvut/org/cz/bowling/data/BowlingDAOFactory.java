package fit.cvut.org.cz.bowling.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

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
            TableUtils.createTableIfNotExists(connectionSource, Frame.class);
            TableUtils.createTableIfNotExists(connectionSource, Roll.class);
            TableUtils.createTableIfNotExists(connectionSource, Participant.class);
            TableUtils.createTableIfNotExists(connectionSource, ParticipantStat.class);
            TableUtils.createTableIfNotExists(connectionSource, PlayerStat.class);
        } catch (SQLException e) {}
    }


    private boolean checkIfColumnExists (SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM" + tableName + "LIMIT 0", null);
            if(cursor.getColumnIndex(columnName) != -1){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private boolean dropTable (Class clazz) {
        try {
            TableUtils.dropTable(connectionSource, clazz, true);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int fromVersion = oldVersion;

        //sorry for dumb implementation of versions <=1.0.4, because in all of them DBVersion is the same and equals "1" - wasn't taken into account at the start of development, that's why we need to control if something exists to differentiate between them
        if (checkIfColumnExists(db, DBConstants.tCONFIGURATIONS, DBConstants.cSIDES_NUMBER) ) {
            fromVersion = 4;
        }

        /*Upgrade from versions <=3 (in reality only 1.0.3 is treated) to version 4*/
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
                IEntityDAO<ParticipantStat, Long> participantStatDAO = ManagerFactory.getInstance().getDaoFactory().getMyDao(ParticipantStat.class);
                try {
                    participantStatDAO.executeRaw("ALTER TABLE `" + DBConstants.tPARTICIPANT_STATS + "` ADD COLUMN " + DBConstants.cFRAMES_NUMBER + " BYTE;"); //mb updateRaw is needed for default value
                } catch (SQLException e) {
                    dropTable(ParticipantStat.class);
                }
                onCreate(db, connectionSource);
                fromVersion++;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int fromVersion = oldVersion; fromVersion > newVersion; --fromVersion){
            switch (fromVersion) {
                case 5: /*from 5 to 4*/
                    try {
                        TableUtils.dropTable(connectionSource, Frame.class, true);
                        TableUtils.dropTable(connectionSource, Frame.class, true);
                    } catch (SQLException e) {}
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