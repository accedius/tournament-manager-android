package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchDAO implements IMatchDAO {

    private ContentValues toContVal(DMatch match)
    {
        ContentValues cv = new ContentValues();
        //cv.put(DBConstants.cID, match.getId());
        cv.put(DBConstants.cTOURNAMENT_ID, match.getTournamentId());
        cv.put(DBConstants.cUID, match.getUid());
        cv.put(DBConstants.cETAG, match.getEtag());
        cv.put(DBConstants.cROUND, match.getRound());
        cv.put(DBConstants.cPERIOD, match.getPeriod());
        cv.put(DBConstants.cPLAYED, match.isPlayed());
        if ( match.getDate() != null )
            cv.put(DBConstants.cDATE, DateFormatFactory.getInstance().getDateFormat().format(match.getDate()));
        if ( match.getLastSynchronized() != null )
            cv.put(DBConstants.cDATE, DateFormatFactory.getInstance().getDateFormat().format(match.getLastSynchronized()));
        cv.put(DBConstants.cLASTMODIFIED, DateFormatFactory.getInstance().getDateFormat().format(new Date()));

        return cv;
    }

    @Override
    public long insert(Context context, DMatch match) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(match);

        long newRowId;
        newRowId = db.insert(DBConstants.tMATCHES, null, values);
        return newRowId;
    }

    @Override
    public void update(Context context, DMatch match) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(match);

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString(match.getId()) };
        db.update(DBConstants.tMATCHES, values, where, projection);
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString( id ) };
        db.delete(DBConstants.tMATCHES, where, projection);
    }

    @Override
    public ArrayList<DMatch> getByTournamentId(Context context, long tournamentId) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( tournamentId ) };
        Cursor cursor = db.query(DBConstants.tMATCHES, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<DMatch> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add( CursorParser.getInstance().parseDMatch(cursor));
        }

        cursor.close();

        return res;
    }


    @Override
    public DMatch getById(Context context, long id) {
        String[] selArgs = { String.valueOf( id ) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        Cursor cursor = db.query( DBConstants.tMATCHES, null, DBConstants.cID + "=?", selArgs, null, null, null );
        cursor.moveToFirst();
        if( cursor.getCount() <= 0 )
            return null;
        DMatch res = CursorParser.getInstance().parseDMatch(cursor);

        cursor.close();

        return res;
    }
}
