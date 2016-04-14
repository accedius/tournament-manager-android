package fit.cvut.org.cz.hockey.data.DAO;

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
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerDAO implements IPackagePlayerDAO {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = new ContentValues();

        values.put( DBConstants.cCOMPETITIONID, competitionId );
        values.put( DBConstants.cPLAYER_ID, playerId );

        Long newRowId;
        newRowId = db.insert(DBConstants.tPLAYERS_IN_COMPETITION, null, values);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = new ContentValues();

        values.put( DBConstants.cTOURNAMENT_ID, tournamentId );
        values.put( DBConstants.cPLAYER_ID, playerId );

        Long newRowId;
        newRowId = db.insert(DBConstants.tPLAYERS_IN_TOURNAMENT, null, values);
    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {

    }

    @Override
    public void addPlayerToTeam(Context context, long playerId, long teamId) {

    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {

    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {

    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {

    }

    @Override
    public void deletePlayerFromTeam(Context context, long playerId, long teamId) {

    }

    @Override
    public ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( competitionId ) };
        Cursor cursor = db.query(DBConstants.tPLAYERS_IN_COMPETITION, null, DBConstants.cCOMPETITIONID + "=?", selArgs, null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add( cursor.getLong( cursor.getColumnIndex(DBConstants.cPLAYER_ID) ));
        }

        cursor.close();

        return res;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( tournamentId ) };
        Cursor cursor = db.query(DBConstants.tPLAYERS_IN_TOURNAMENT, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add( cursor.getLong( cursor.getColumnIndex(DBConstants.cPLAYER_ID) ));
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
        return null;
    }

    @Override
    public Map<Long, DPlayer> getAllPlayers(Context context) {

        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String[] projection = new String[] {DBConstants.cID, DBConstants.cNAME, DBConstants.cETAG, DBConstants.cUID, DBConstants.cLASTMODIFIED, DBConstants.cEMAIL, DBConstants.cNOTE};

            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers);

            Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
            Map<Long, DPlayer> players = new HashMap<>();
            while (c.moveToNext()){
                DPlayer player = CursorParser.getInstance().parseDPlayer(c);
                players.put(player.getId(), player);
            }
            c.close();

            return players;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
