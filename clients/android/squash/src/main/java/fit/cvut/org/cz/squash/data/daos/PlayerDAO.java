package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 *
 * Provides implementation of interface over SQLite database
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerDAO implements IPackagePlayerDAO {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cCOMPETITION_ID, competitionId);
        cv.put(DBConstants.cPLAYER_ID, playerId);

        db.insert(DBConstants.tPLAYERS_IN_COMPETITION, null, cv);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cTOURNAMENT_ID, tournamentId);
        cv.put(DBConstants.cPLAYER_ID, playerId);

        db.insert(DBConstants.tPLAYERS_IN_TOURNAMENT, null, cv);
    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {
    }

    @Override
    public void addPlayerToTeam(Context context, long playerId, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cTEAM_ID, teamId);
        cv.put(DBConstants.cPLAYER_ID, playerId);

        db.insert(DBConstants.tPLAYERS_IN_TEAM, null, cv);
    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ? AND %s = ?", DBConstants.cCOMPETITION_ID, DBConstants.cPLAYER_ID);
        String[] args = new String[]{Long.toString(competitionId), Long.toString(playerId)};

        db.delete(DBConstants.tPLAYERS_IN_COMPETITION, where, args);
    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ? AND %s = ?", DBConstants.cTOURNAMENT_ID, DBConstants.cPLAYER_ID);
        String[] args = new String[]{Long.toString(tournamentId), Long.toString(playerId)};

        db.delete(DBConstants.tPLAYERS_IN_TOURNAMENT, where, args);
    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {
    }

    @Override
    public void deleteAllPlayersFromTeam(Context context, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ?", DBConstants.cTEAM_ID);
        String[] args = new String[]{Long.toString(teamId)};

        db.delete(DBConstants.tPLAYERS_IN_TEAM, where, args);
    }

    @Override
    public ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPLAYERS_IN_COMPETITION, DBConstants.cCOMPETITION_ID),
                new String[]{Long.toString(competitionId)});

        ArrayList<Long> ids = new ArrayList<>();

        while (c.moveToNext())
            ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        return ids;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPLAYERS_IN_TOURNAMENT, DBConstants.cTOURNAMENT_ID),
                new String[]{Long.toString(tournamentId)});

        ArrayList<Long> ids = new ArrayList<>();

        while (c.moveToNext())
            ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        return ids;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId) {
        return null;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTeam(Context context, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPLAYERS_IN_TEAM, DBConstants.cTEAM_ID),
                new String[]{Long.toString(teamId)});

        ArrayList<Long> ids = new ArrayList<>();

        while (c.moveToNext())
            ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        return ids;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByParticipant(Context context, long participantId) {
        return null;
    }

    @Override
    public Map<Long, Player> getAllPlayers(Context context) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String[] projection = new String[] {DBConstants.cID, DBConstants.cNAME, DBConstants.cETAG, DBConstants.cUID, DBConstants.cLASTMODIFIED, DBConstants.cEMAIL, DBConstants.cNOTE, DBConstants.cLASTSYNCHRONIZED};

            Cursor c = context.getContentResolver().query(Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers), projection, null, null, null);
            Map<Long, Player> players = new HashMap<>();
            while (c.moveToNext()){
                Player player = CursorParser.getInstance().parsePlayer(c);
                players.put(player.getId(), player);
            }
            c.close();

            return players;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long insertPlayer(Context context, ContentValues values) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers);
            Uri insertUri = context.getContentResolver().insert(uri, values);
            return ContentUris.parseId(insertUri);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @Override
    public void updatePlayer(Context context, ContentValues values) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String where = DBConstants.tPLAYERS + "." + DBConstants.cEMAIL + " = '" + values.getAsString("email")+"'";
            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayerUpdate);
            context.getContentResolver().update(uri, values, where, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
