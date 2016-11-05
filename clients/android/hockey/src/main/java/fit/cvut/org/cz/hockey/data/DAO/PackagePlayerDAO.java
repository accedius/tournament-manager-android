package fit.cvut.org.cz.hockey.data.DAO;

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

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerDAO implements IPackagePlayerDAO {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        ContentValues values = new ContentValues();

        values.put(DBConstants.cCOMPETITIONID, competitionId);
        values.put(DBConstants.cPLAYER_ID, playerId);

        Long newRowId = db.insert(DBConstants.tPLAYERS_IN_COMPETITION, null, values);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        ContentValues values = new ContentValues();

        values.put(DBConstants.cTOURNAMENT_ID, tournamentId);
        values.put(DBConstants.cPLAYER_ID, playerId);

        Long newRowId = db.insert(DBConstants.tPLAYERS_IN_TOURNAMENT, null, values);
    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {
    }

    @Override
    public void addPlayerToTeam(Context context, long playerId, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        ContentValues values = new ContentValues();
        values.put(DBConstants.cTEAM_ID, teamId);
        values.put(DBConstants.cPLAYER_ID, playerId);

        Long newRowId = db.insert(DBConstants.tPLAYERS_IN_TEAM, null, values);
    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        String where = String.format("%s = ? AND %s = ?", DBConstants.cPLAYER_ID, DBConstants.cCOMPETITIONID);
        String[] projection = new String[]{ Long.toString(playerId), Long.toString(competitionId) };
        db.delete(DBConstants.tPLAYERS_IN_COMPETITION, where, projection);
    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        String where = String.format("%s = ? AND %s = ?", DBConstants.cPLAYER_ID, DBConstants.cTOURNAMENT_ID);
        String[] projection = new String[]{ Long.toString(playerId), Long.toString(tournamentId) };
        db.delete(DBConstants.tPLAYERS_IN_TOURNAMENT, where, projection);
    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {
    }

    @Override
    public void deleteAllPlayersFromTeam(Context context, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);

        String where = String.format("%s = ?", DBConstants.cTEAM_ID);
        String[] projection = new String[]{ Long.toString(teamId) };
        db.delete(DBConstants.tPLAYERS_IN_TEAM, where, projection);
    }

    @Override
    public ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);
        ArrayList<Long> res = new ArrayList<>();
        String[] selArgs = { String.valueOf(competitionId) };
        Cursor cursor = db.query(DBConstants.tPLAYERS_IN_COMPETITION, null, DBConstants.cCOMPETITIONID + "=?", selArgs, null, null, null);
        while (cursor.moveToNext()) {
            res.add(cursor.getLong(cursor.getColumnIndex(DBConstants.cPLAYER_ID)));
        }
        cursor.close();
        return res;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);
        String[] selArgs = { String.valueOf(tournamentId) };
        Cursor cursor = db.query(DBConstants.tPLAYERS_IN_TOURNAMENT, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getLong(cursor.getColumnIndex(DBConstants.cPLAYER_ID)));
        }

        cursor.close();
        return res;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId) {
        return null;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTeam(Context context, long teamId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);
        String[] selArgs = { String.valueOf(teamId) };
        Cursor cursor = db.query(DBConstants.tPLAYERS_IN_TEAM, null, DBConstants.cTEAM_ID + "=?", selArgs, null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getLong(cursor.getColumnIndex(DBConstants.cPLAYER_ID)));
        }

        cursor.close();
        return res;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByParticipant(Context context, long participantId) {
        SQLiteDatabase db = DatabaseFactory.getDatabase(context);
        String[] selArgs = { String.valueOf(participantId), StatsEnum.participates.toString()};
        Cursor cursor = db.query(DBConstants.tSTATS, null, DBConstants.cPARTICIPANT_ID + "=? AND " + DBConstants.cSTATS_ENUM_ID + "=?", selArgs, null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(cursor.getLong(cursor.getColumnIndex(DBConstants.cPLAYER_ID)));
        }

        cursor.close();
        return res;
    }

    @Override
    public Map<Long, Player> getAllPlayers(Context context) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String[] projection = new String[] {DBConstants.cID, DBConstants.cNAME, DBConstants.cETAG, DBConstants.cUID, DBConstants.cLASTMODIFIED, DBConstants.cLASTSYNCHRONIZED, DBConstants.cEMAIL, DBConstants.cNOTE};

            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers);

            Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
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
