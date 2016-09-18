package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.*;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;

import fit.cvut.org.cz.tmlibrary.business.DateFormatter;

/** Provides implementation of interface over SQLite database
 * Created by Vaclav on 21. 4. 2016.
 */
public class MatchDAO implements IMatchDAO {

    private ContentValues convert(DMatch match){
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cROUND, match.getRound());
        cv.put(DBConstants.cPERIOD, match.getPeriod());
        cv.put(DBConstants.cPLAYED, match.isPlayed());
        cv.put(DBConstants.cTOURNAMENT_ID, match.getTournamentId());
        cv.put(DBConstants.cNOTE, match.getNote());
        if (match.getDate() != null)
            cv.put(DBConstants.cDATE, dateFormat.format(match.getDate()));
        cv.put(DBConstants.cUID, match.getUid());
        cv.put(DBConstants.cETAG, match.getEtag());
        if (match.getLastModified() != null) cv.put(DBConstants.cLASTMODIFIED, dateFormat.format(match.getLastModified()));
        if (match.getLastSynchronized() != null) cv.put(DBConstants.cLASTSYNCHRONIZED, dateFormat.format(match.getLastSynchronized()));

        return cv;
    }

    @Override
    public long insert(Context context, DMatch match) {
        ContentValues cv = convert(match);
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        long id = db.insert(DBConstants.tMATCHES, null, cv);
        db.close();
        return id;
    }

    @Override
    public void update(Context context, DMatch match) {
        ContentValues cv = convert(match);
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        db.update(DBConstants.tMATCHES, cv, String.format("%s = ?", DBConstants.cID), new String[] {Long.toString(match.getId())});
        db.close();

    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cID);
        db.delete(DBConstants.tMATCHES, where, new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public ArrayList<DMatch> getByTournamentId(Context context, long tournamentId) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ?", DBConstants.tMATCHES, DBConstants.cTOURNAMENT_ID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(tournamentId)});

        ArrayList<DMatch> matches = new ArrayList<>();

        while (c.moveToNext())
            matches.add(CursorParser.getInstance().parseDMatch(c));

        c.close();
        db.close();

        return matches;
    }

    @Override
    public DMatch getById(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String selection = String.format("select * from %s where %s = ?", DBConstants.tMATCHES, DBConstants.cID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(id)});
        DMatch match = null;

        if (c.moveToNext())
            match = CursorParser.getInstance().parseDMatch(c);

        c.close();
        db.close();

        return match;
    }
}
