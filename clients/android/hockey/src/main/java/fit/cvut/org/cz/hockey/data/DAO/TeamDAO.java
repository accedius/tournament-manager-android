package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamDAO implements ITeamDAO {
    private static SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

    private ContentValues toContVal(DTeam team) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, team.getName());
        cv.put(DBConstants.cTOURNAMENT_ID, team.getTournamentId());
        cv.put(DBConstants.cUID, team.getUid());
        cv.put(DBConstants.cETAG, team.getEtag());
        cv.put(DBConstants.cLASTMODIFIED, dateTimeFormat.format(new Date()));
        if (team.getLastSynchronized() != null)
            cv.put(DBConstants.cLASTSYNCHRONIZED, dateTimeFormat.format(team.getLastSynchronized()));

        return cv;
    }

    @Override
    public void insert(Context context, DTeam team) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(team);

        Long newRowId;
        newRowId = db.insert(DBConstants.tTEAMS, null, values);

        db.close();
    }

    @Override
    public void update(Context context, DTeam team) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(team);

        values.put(DBConstants.cID, team.getId());

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(team.getId()) };
        db.update(DBConstants.tTEAMS, values, where, projection);
        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(id) };
        db.delete(DBConstants.tTEAMS, where, projection);
        db.close();
    }

    @Override
    public DTeam getById(Context context, long id) {
        String[] selArgs = { String.valueOf(id) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor cursor = db.query(DBConstants.tTEAMS, null, DBConstants.cID + "=?", selArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0)
            return null;
        DTeam res = CursorParser.getInstance().parseDTeam(cursor);

        cursor.close();
        db.close();

        return res;
    }

    @Override
    public ArrayList<DTeam> getByTournamentId(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { String.valueOf(tournamentId) };
        Cursor cursor = db.query(DBConstants.tTEAMS, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<DTeam> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add(CursorParser.getInstance().parseDTeam(cursor));
        }

        cursor.close();
        db.close();

        return res;
    }
}
