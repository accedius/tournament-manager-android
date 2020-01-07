package fit.cvut.org.cz.bowling.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;


import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class BowlingDAOFactory extends DAOFactory implements IDAOFactory {
    private static final int DBVersion = 6;

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
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0 ", null);
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

    public boolean dropTable (Class clazz) {
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

        //check if it's version 4 or older
        checkForOldVersions(db, fromVersion);

        //from <=3 to 4
        upgradeFromLT3To4(db, fromVersion);

        switch (fromVersion) {
            case 4: /*from 4 to 5*/
                upgradeFrom4To5(db, fromVersion);
            case 5:
                upgradeFrom5To6(db, fromVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Competition.class, true);
            TableUtils.dropTable(connectionSource, CompetitionPlayer.class, true);
            TableUtils.dropTable(connectionSource, Tournament.class, true);
            TableUtils.dropTable(connectionSource, TournamentPlayer.class, true);
            TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            TableUtils.dropTable(connectionSource, Team.class, true);
            TableUtils.dropTable(connectionSource, TeamPlayer.class, true);
            TableUtils.dropTable(connectionSource, Match.class, true);
            TableUtils.dropTable(connectionSource, Frame.class, true);
            TableUtils.dropTable(connectionSource, Roll.class, true);
            TableUtils.dropTable(connectionSource, Participant.class, true);
            TableUtils.dropTable(connectionSource, ParticipantStat.class, true);
            TableUtils.dropTable(connectionSource, PlayerStat.class, true);
        } catch (SQLException e) {}
        onCreate(db, connectionSource);
        /*for(int fromVersion = oldVersion; fromVersion > newVersion; --fromVersion){
            switch (fromVersion) {
                case 5: //from 5 to 4
                    try {
                        TableUtils.dropTable(connectionSource, Frame.class, true);
                        TableUtils.dropTable(connectionSource, Roll.class, true);
                    } catch (SQLException e) {}
                    break;
                case 4: //from 4 to 3 (and less)
                    try {
                        TableUtils.dropTable(connectionSource, Match.class, true);
                        TableUtils.dropTable(connectionSource, PlayerStat.class, true);
                        TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
                    } catch (SQLException e) {}
                    onCreate(db, connectionSource);
                    fromVersion = 1;
                    break;
            }
        }*/
    }

    /**
     * Sorry for dumb implementation of versions <=1.0.4, because in all of them DBVersion is the same and equals "1" - wasn't taken into account at the start of development, that's why we need to control if something exists to differentiate between them
     * @param db
     * @param fromVersion
     */
    private void checkForOldVersions (SQLiteDatabase db, int fromVersion) {
        if (fromVersion < 5 && checkIfColumnExists(db, DBConstants.tCONFIGURATIONS, DBConstants.cSIDES_NUMBER) ) {
            fromVersion = 4;
        }
    }

    /**
     * Upgrade from versions <=3 (in reality only 1.0.3 is treated) to version 4
     * @param db
     * @param fromVersion
     */
    private void upgradeFromLT3To4 (SQLiteDatabase db, int fromVersion) {
        if(fromVersion <= 3) {
            try {
                TableUtils.dropTable(connectionSource, Match.class, true);
                TableUtils.dropTable(connectionSource, PlayerStat.class, true);
                TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            } catch (SQLException e) {}
            onCreate(db, connectionSource);
            fromVersion = 4;
        }
    }

    /**
     * Deletes all table contents with manager methods - use, then there are dependencies between tables/classes for safe delete
     * @param iManagerFactory
     * @param clazz class object of a table to be purged
     * @param <E> table class
     * @param <M> class iManager
     * @return is successful
     */
    public <E extends IEntity, M extends IManager<E>> boolean deleteTableContents (IManagerFactory iManagerFactory, Class<E> clazz) {
        M iClassManager = iManagerFactory.getEntityManager(clazz);
        List<E> tableContents = iClassManager.getAll();
        for(E object : tableContents) {
            long objectId = object.getId();
            iClassManager.delete(objectId);
        }
        return true;
    }

    /**
     * Upgrade script for moving from version 4 to 5
     * @param db
     * @param fromVersion
     */
    private void upgradeFrom4To5 (SQLiteDatabase db, int fromVersion) {
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IEntityDAO<ParticipantStat, Long> participantStatDAO = iManagerFactory.getDaoFactory().getMyDao(ParticipantStat.class);
        try {
            if (!checkIfColumnExists(db, DBConstants.tPARTICIPANT_STATS, DBConstants.cFRAMES_NUMBER))
                participantStatDAO.executeRaw("ALTER TABLE " + DBConstants.tPARTICIPANT_STATS + " ADD COLUMN " + DBConstants.cFRAMES_NUMBER + " BYTE DEFAULT 0;");
        } catch (SQLException e) {
            dropTable(ParticipantStat.class);
        }
        IEntityDAO<Match, Long> matchDAO = iManagerFactory.getDaoFactory().getMyDao(Match.class);
        try {
            if (!checkIfColumnExists(db, DBConstants.tMATCHES, DBConstants.cVALID_FOR_STATS)) {
                matchDAO.executeRaw("ALTER TABLE " + DBConstants.tMATCHES + " ADD COLUMN " + DBConstants.cVALID_FOR_STATS + " BOOLEAN DEFAULT 0;"); // SQLite stores Booleans as Integers 0 -> false; 1 -> true
                matchDAO.executeRaw("ALTER TABLE " + DBConstants.tMATCHES + " ADD COLUMN " + DBConstants.cTRACK_ROLLS + " BOOLEAN DEFAULT 0;"); // SQLite stores Booleans as Integers 0 -> false; 1 -> true
            }
        } catch (SQLException e) {}
        dropTable(ParticipantStat.class);
        dropTable(PlayerStat.class);
        dropTable(Participant.class);
        dropTable(Match.class);
        onCreate(db, connectionSource);

        /*we need to delete all previously created matches - easy solution to avoid troubles, anyway there is no stats in them (they are empty - <=1.0.4 there was no way to edit/view stats).
        deleteTableContents(iManagerFactory, Match.class);
        or
        IMatchManager iMatchManager = iManagerFactory.getEntityManager(Match.class);
        List<Match> allMatches= iMatchManager.getAll();
        for(Match match : allMatches) {
            iMatchManager.delete(match.getId());
        }*/

        ++fromVersion;
    }

    /**
     * Upgrade script for moving from version 5 to 6
     * @param db
     * @param fromVersion
     */
    private void upgradeFrom5To6 (SQLiteDatabase db, int fromVersion) {
        dropTable(ParticipantStat.class);
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IEntityDAO<Participant, Long> participantDao = iManagerFactory.getDaoFactory().getMyDao(Participant.class);

        try {
            participantDao.deleteItemById(DBConstants.cROLE, ParticipantType.home.toString());
        } catch (SQLException e) {}

        onCreate(db, connectionSource);
        ++fromVersion;
    }
}