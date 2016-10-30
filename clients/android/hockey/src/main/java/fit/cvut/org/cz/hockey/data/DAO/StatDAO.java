package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IStatDAO;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class StatDAO implements IStatDAO {
    private ContentValues toContVal(DStat stat) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cVALUE, stat.getValue());
        if (stat.getPlayerId() != -1 && stat.getPlayerId() != 0) cv.put(DBConstants.cPLAYER_ID, stat.getPlayerId());
        cv.put(DBConstants.cPARTICIPANT_ID, stat.getParticipantId());
        cv.put(DBConstants.cSTATS_ENUM_ID, stat.getStatsEnumId());
        cv.put(DBConstants.cTOURNAMENT_ID, stat.getTournamentId());
        cv.put(DBConstants.cCOMPETITIONID, stat.getCompetitionId());
        return cv;
    }

    @Override
    public long insert(Context context, DStat stat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(stat);

        Long newRowId;
        newRowId = db.insert(DBConstants.tSTATS, null, values);
        return newRowId;
    }

    @Override
    public void update(Context context, DStat stat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(stat);

        values.put(DBConstants.cID, stat.getId());

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(stat.getId()) };
        db.update(DBConstants.tSTATS, values, where, projection);
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(id) };
        db.delete(DBConstants.tSTATS, where, projection);
    }

    @Override
    public ArrayList<DStat> getStatsByPlayerId(Context context, long playerId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { String.valueOf(playerId) };
        Cursor cursor = db.query(DBConstants.tSTATS, null, DBConstants.cPLAYER_ID + "=?", selArgs, null, null, null);

        ArrayList<DStat> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(CursorParser.getInstance().parseDStat(cursor));
        }

        cursor.close();
        return res;
    }

    @Override
    public ArrayList<DStat> getStatsByParticipantId(Context context, long participantId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { String.valueOf(participantId) };
        Cursor cursor = db.query(DBConstants.tSTATS, null, DBConstants.cPARTICIPANT_ID + "=?", selArgs, null, null, null);

        ArrayList<DStat> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(CursorParser.getInstance().parseDStat(cursor));
        }

        cursor.close();
        return res;
    }

    @Override
    public ArrayList<DStat> getStatsByTournamentId(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { String.valueOf(tournamentId) };
        Cursor cursor = db.query(DBConstants.tSTATS, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<DStat> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(CursorParser.getInstance().parseDStat(cursor));
        }

        cursor.close();
        return res;
    }

    @Override
    public ArrayList<DStat> getStatsByCompetitionId(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { String.valueOf(competitionId) };
        Cursor cursor = db.query(DBConstants.tSTATS, null, DBConstants.cCOMPETITIONID + "=?", selArgs, null, null, null);

        ArrayList<DStat> res = new ArrayList<>();

        while (cursor.moveToNext()) {
            res.add(CursorParser.getInstance().parseDStat(cursor));
        }

        cursor.close();
        return res;
    }
}
