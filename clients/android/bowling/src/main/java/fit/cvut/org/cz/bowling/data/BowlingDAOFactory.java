package fit.cvut.org.cz.bowling.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class BowlingDAOFactory extends DAOFactory implements IDAOFactory {
    private static final int DBVersion = 10;

    public BowlingDAOFactory(Context context, String name) {
        super(context, name, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        Log.d("BowlingDAOFactory", "onCreate: starting" );

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
            TableUtils.createTableIfNotExists(connectionSource, WinCondition.class);
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "onCreate: FAILED"); e.printStackTrace();}
    }


    private boolean checkIfColumnExists (SQLiteDatabase db, String tableName, String columnName) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0 ", null);
            if(cursor.getColumnIndex(columnName) != -1){
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e("BowlingDAOFactory", "checkIfColumnExists: failed on db = " + db + ", tableName = " + tableName + ", columnName = " + columnName );
            e.printStackTrace();
            throw e;
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private boolean dropTable (Class clazz) throws SQLException {
        try {
            TableUtils.dropTable(connectionSource, clazz, true);
        } catch (SQLException e) {
            Log.e("BowlingDAOFactory", "dropTable: failed on deleting table for class " + clazz.getName() );
            e.printStackTrace();
            throw e;
        }
        return true;
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

    private <E extends IEntity, DAO extends IEntityDAO<E, Long> > boolean upgradeTableToSupportShareBase (SQLiteDatabase db, Class<E> clazz, String tableName) throws SQLException {
        Log.d("BowlingDAOFactory", "upgradeTableToSupportShareBase: " + tableName + " obtaining class DAO" );
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        DAO classDAO = iManagerFactory.getDaoFactory().getMyDao(clazz);
        if(classDAO == null) {
            SQLException exception = new SQLException();
            StackTraceElement[] trace = new StackTraceElement[] {
                    new StackTraceElement("BowlingDAOFactory", "upgradeTableToSupportShareBase", "BowlingDAOFactory.java", 123)
            };
            exception.setStackTrace(trace);
            Log.e("BowlingDAOFactory", "upgradeTableToSupportShareBase: " + tableName + " FAILED on obtaining class DAO for class " + clazz.getName());
            exception.printStackTrace();
            throw exception;
        }

        Log.d("BowlingDAOFactory", "upgradeTableToSupportShareBase: " + tableName + " starting" );
        List<String> columnNames = new ArrayList<>();
        columnNames.add(DBConstants.cETAG);
        columnNames.add(DBConstants.cLASTMODIFIED);
        columnNames.add(DBConstants.cLASTSYNCHRONIZED);
        columnNames.add(DBConstants.cTOKEN_TYPE);
        columnNames.add(DBConstants.cTOKEN_VALUE);
        columnNames.add(DBConstants.cUID);

        for(String columnName : columnNames) {
            if (!checkIfColumnExists(db, tableName, columnName)) {
                Log.d("BowlingDAOFactory", "upgradeTableToSupportShareBase: " + tableName + " altering " + columnName);
                try {
                    classDAO.executeRaw("ALTER TABLE " + tableName + " ADD COLUMN `" + columnName + "` VARCHAR ;");
                } catch (SQLException e) {
                    Log.e("BowlingDAOFactory", "upgradeTableToSupportShareBase: " + tableName + " FAILED on column " + columnName);
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int fromVersion = oldVersion;

        Log.d("BowlingDAOFactory", "onUpgrade: starting" );

        try {
            if(fromVersion < 5) {
                //check if it's version 4 or older
                fromVersion = checkForOldVersions(db, fromVersion);

                //from <=3 to 4
                fromVersion = upgradeFromLT3To4(db, fromVersion);
            }

            switch (fromVersion) {
                case 4: /*from 4 to 5*/
                    fromVersion = upgradeFrom4To5(db, fromVersion);
                case 5:
                    fromVersion = upgradeFrom5To6(db, fromVersion);
                case 6:
                    fromVersion = upgradeFrom6To7(db, fromVersion);
                case 7:
                    fromVersion = upgradeFrom7To8(db, fromVersion);
                case 8:
                    fromVersion = upgradeFrom8To9(db, fromVersion);
                case 9:
                    fromVersion = upgradeFrom9To10(db, fromVersion);
            }

        } catch (SQLException e) {Log.e("BowlingDAOFactory", "onUpgrade: FAILED from " + oldVersion + " to " + newVersion + " on cycle value fromVersion = " + fromVersion);}

        onCreate(db, connectionSource);

        Log.d("BowlingDAOFactory", "onUpgrade: SUCCESS" );
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("BowlingDAOFactory", "onDowngrade: starting" );

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
            TableUtils.dropTable(connectionSource, WinCondition.class, true);
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "onDowngrade: FAILED from " + oldVersion + " to " + newVersion); e.printStackTrace();}
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
    private int checkForOldVersions (SQLiteDatabase db, final int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "checkForOldVersions: starting" );

        try {
            if (version < 5 && checkIfColumnExists(db, DBConstants.tCONFIGURATIONS, DBConstants.cSIDES_NUMBER)) {
                version = 4;
            }
        } catch (SQLException e) {
            Log.e("BowlingDAOFactory", "checkForOldVersions: FAILED" );
            throw e;
        }
        return version;
    }

    /**
     * Upgrade from versions <=3 (in reality only 1.0.3 is treated) to version 4
     * @param db
     * @param fromVersion
     */
    private int upgradeFromLT3To4 (SQLiteDatabase db, int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFromLT3To4: starting" );

        if(version <= 3) {
            try {
                TableUtils.dropTable(connectionSource, Match.class, true);
                TableUtils.dropTable(connectionSource, PlayerStat.class, true);
                TableUtils.dropTable(connectionSource, PointConfiguration.class, true);
            } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFromLT3To4: FAILED on dropping tables with wrong content" ); e.printStackTrace(); throw e;}
            onCreate(db, connectionSource);
            version = 4;
        }
        return version;
    }

    /**
     * Upgrade script for moving from version 4 to 5
     * @param db
     * @param fromVersion
     */
    private int upgradeFrom4To5 (SQLiteDatabase db, int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFrom5To6: starting" );

        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IEntityDAO<ParticipantStat, Long> participantStatDAO = iManagerFactory.getDaoFactory().getMyDao(ParticipantStat.class);
        try {
            if (!checkIfColumnExists(db, DBConstants.tPARTICIPANT_STATS, DBConstants.cFRAMES_NUMBER))
                participantStatDAO.executeRaw("ALTER TABLE " + DBConstants.tPARTICIPANT_STATS + " ADD COLUMN " + DBConstants.cFRAMES_NUMBER + " BYTE DEFAULT 0;");
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFrom4To5: FAILED on altering ParticipantStat.class table" ); e.printStackTrace(); throw e;}
        IEntityDAO<Match, Long> matchDAO = iManagerFactory.getDaoFactory().getMyDao(Match.class);
        try {
            if (!checkIfColumnExists(db, DBConstants.tMATCHES, DBConstants.cVALID_FOR_STATS)) {
                matchDAO.executeRaw("ALTER TABLE " + DBConstants.tMATCHES + " ADD COLUMN `" + DBConstants.cVALID_FOR_STATS + "` BOOLEAN DEFAULT 0;"); // SQLite stores Booleans as Integers 0 -> false; 1 -> true
                matchDAO.executeRaw("ALTER TABLE " + DBConstants.tMATCHES + " ADD COLUMN `" + DBConstants.cTRACK_ROLLS + "` BOOLEAN DEFAULT 0;"); // SQLite stores Booleans as Integers 0 -> false; 1 -> true
            }
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFrom4To5: FAILED on altering Match.class table" ); e.printStackTrace(); throw e;}
        //to be 100% sure
        try {
            TableUtils.dropTable(connectionSource, ParticipantStat.class, true);
            TableUtils.dropTable(connectionSource, PlayerStat.class, true);
            TableUtils.dropTable(connectionSource, Participant.class, true);
            TableUtils.dropTable(connectionSource, Match.class, true);
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFrom4To5: FAILED on removing tables with wrong content" ); e.printStackTrace(); throw e;}
        onCreate(db, connectionSource);

        /*we need to delete all previously created matches - easy solution to avoid troubles, anyway there is no stats in them (they are empty - <=1.0.4 there was no way to edit/view stats).
        deleteTableContents(iManagerFactory, Match.class);
        or
        IMatchManager iMatchManager = iManagerFactory.getEntityManager(Match.class);
        List<Match> allMatches= iMatchManager.getAll();
        for(Match match : allMatches) {
            iMatchManager.delete(match.getId());
        }*/

        ++version;
        return version;
    }

    /**
     * Upgrade script for moving from version 5 to 6
     * @param db
     * @param fromVersion
     */
    private int upgradeFrom5To6 (SQLiteDatabase db, int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFrom5To6: starting" );

        dropTable(ParticipantStat.class);
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IEntityDAO<Participant, Long> participantDao = iManagerFactory.getDaoFactory().getMyDao(Participant.class);

        try {
            participantDao.deleteItemById(DBConstants.cROLE, ParticipantType.home.toString());
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFrom5To6: FAILED on removing wrong participants" ); e.printStackTrace(); throw e;}

        onCreate(db, connectionSource);
        ++version;
        return version;
    }

    private int upgradeFrom6To7 (SQLiteDatabase db, int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFrom6To7: starting" );

        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IEntityDAO<PlayerStat, Long> matchDAO = iManagerFactory.getDaoFactory().getMyDao(PlayerStat.class);
        try {
            if (!checkIfColumnExists(db, DBConstants.tPLAYER_STATS, DBConstants.cFRAMES_NUMBER)) {
                matchDAO.executeRaw("ALTER TABLE " + DBConstants.tPLAYER_STATS + " ADD COLUMN `" + DBConstants.cFRAMES_NUMBER + "` BYTE DEFAULT 0;");
            }
        } catch (SQLException e) {Log.e("BowlingDAOFactory", "upgradeFrom6To7: FAILED on altering PlayerStat.class table" ); e.printStackTrace(); throw e;}
        ++version;
        return version;
    }

    private int upgradeFrom7To8(SQLiteDatabase db, int fromVersion) {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFrom7To8: creating new tables" );

        onCreate(db, connectionSource);
        ++version;
        return version;
    }

    private int upgradeFrom8To9 (SQLiteDatabase db, int fromVersion) throws SQLException {
        int version = fromVersion;

        Log.d("BowlingDAOFactory", "upgradeFrom8To9: upgrading tables to support ShareBase" );

        String tConfigurations = DBConstants.tCONFIGURATIONS;
        if( ! ( checkIfColumnExists(db, tConfigurations, DBConstants.cETAG ) && checkIfColumnExists(db, tConfigurations, DBConstants.cLASTMODIFIED) && checkIfColumnExists(db, tConfigurations, DBConstants.cLASTSYNCHRONIZED) && checkIfColumnExists(db, tConfigurations, DBConstants.cTOKEN_TYPE) && checkIfColumnExists(db, tConfigurations, DBConstants.cTOKEN_VALUE) && checkIfColumnExists(db, tConfigurations, DBConstants.cUID) ) ) {
            upgradeTableToSupportShareBase(db, PointConfiguration.class, tConfigurations);
        }

        String tFrames = DBConstants.tMATCH_FRAMES;
        if( ! ( checkIfColumnExists(db, tFrames, DBConstants.cETAG ) && checkIfColumnExists(db, tFrames, DBConstants.cLASTMODIFIED) && checkIfColumnExists(db, tFrames, DBConstants.cLASTSYNCHRONIZED) && checkIfColumnExists(db, tFrames, DBConstants.cTOKEN_TYPE) && checkIfColumnExists(db, tFrames, DBConstants.cTOKEN_VALUE) && checkIfColumnExists(db, tFrames, DBConstants.cUID) ) ) {
            upgradeTableToSupportShareBase(db, Frame.class, tFrames);
        }

        String tRolls = DBConstants.tMATCH_FRAME_ROLLS;
        if( ! ( checkIfColumnExists(db, tRolls, DBConstants.cETAG ) && checkIfColumnExists(db, tRolls, DBConstants.cLASTMODIFIED) && checkIfColumnExists(db, tRolls, DBConstants.cLASTSYNCHRONIZED) && checkIfColumnExists(db, tRolls, DBConstants.cTOKEN_TYPE) && checkIfColumnExists(db, tRolls, DBConstants.cTOKEN_VALUE) && checkIfColumnExists(db, tRolls, DBConstants.cUID) ) ) {
            upgradeTableToSupportShareBase(db, Roll.class, tRolls);
        }

        ++version;
        return version;
    }

     private int upgradeFrom9To10 (SQLiteDatabase db, int fromVersion) throws SQLException {
         int version = fromVersion;

         Log.d("BowlingDAOFactory", "upgradeFrom9To10: checking and fixing problems, caused by match generation in older version (<1.0.6)" );

         IManagerFactory iManagerFactory = ManagerFactory.getInstance();
         ITournamentManager tournamentManager = iManagerFactory.getEntityManager(Tournament.class);
         IParticipantManager participantManager = iManagerFactory.getEntityManager(Participant.class);
         IParticipantStatManager participantStatManager = iManagerFactory.getEntityManager(ParticipantStat.class);
         IPlayerStatManager playerStatManager = iManagerFactory.getEntityManager(PlayerStat.class);
         IMatchManager matchManager = iManagerFactory.getEntityManager(Match.class);

         List<Match> allMatchesRaw = matchManager.getAll();
         for (Match rawMatch : allMatchesRaw) {
             long matchId = rawMatch.getId();
             List<Participant> matchParticipants = participantManager.getByMatchIdWithPlayerStats(matchId);
             for(Participant participant : matchParticipants) {
                 long participantId = participant.getId();
                 long realParticipantId = participant.getParticipantId();
                 List<ParticipantStat> participantStats = (List<ParticipantStat>) participant.getParticipantStats();
                 List<PlayerStat> playerStats = (List<PlayerStat>) participant.getPlayerStats();
                 if(participantStats.isEmpty()) {
                     Log.d("BowlingDAOFactory", "upgradeFrom9To10: adding participantStat for " + participant.getName() + " in match with Id = " + matchId);
                     ParticipantStat participantStat = new ParticipantStat(participantId);
                     participantStats.add(participantStat);
                     participantStatManager.insert(participantStat);
                 }
                 if(playerStats.isEmpty()) {
                     Tournament tournament = tournamentManager.getById(rawMatch.getTournamentId());
                     if(tournament.getTypeId() == TournamentTypes.type_individuals) {
                         Log.d("BowlingDAOFactory", "upgradeFrom9To10: adding playerStat for " + participant.getName() + " in match for individuals (must be so) with Id = " + matchId + ", tournament name is " + tournament.getName());
                         PlayerStat playerStat = new PlayerStat(participantId, realParticipantId);
                         ParticipantStat participantStat = participantStats.get(0);
                         int score = participantStat.getScore();
                         byte frames = participantStat.getFramesPlayedNumber();
                         playerStat.setPoints(score);
                         playerStat.setFramesPlayedNumber(frames);
                         playerStatManager.insert(playerStat);
                     }
                 }
             }
         }

         ++version;
         return version;
     }
}