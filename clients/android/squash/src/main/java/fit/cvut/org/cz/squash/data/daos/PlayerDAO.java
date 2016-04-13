package fit.cvut.org.cz.squash.data.daos;

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
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerDAO implements IPackagePlayerDAO {

    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cCOMPETITIONID, competitionId);
        cv.put(DBConstants.cPLAYER_ID, playerId);

        db.insert(DBConstants.tPLAYERS_IN_COMPETITION, null, cv);
        db.close();
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cTOURNAMENT_ID, tournamentId);
        cv.put(DBConstants.cPLAYER_ID, playerId);

        db.insert(DBConstants.tPLAYERS_IN_TOURNAMENT, null, cv);
        db.close();
    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {

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
    public ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPLAYERS_IN_COMPETITION, DBConstants.cCOMPETITIONID),
                new String[]{Long.toString(competitionId)});

        ArrayList<Long> ids = new ArrayList<>();

        while (c.moveToNext()) ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        db.close();

        return ids;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPLAYERS_IN_TOURNAMENT, DBConstants.cPLAYER_ID),
                new String[]{Long.toString(tournamentId)});

        ArrayList<Long> ids = new ArrayList<>();

        while (c.moveToNext()) ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        db.close();

        return ids;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId) {
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

            Cursor c = context.getContentResolver().query(Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers), projection, null, null, null);
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
