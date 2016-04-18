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
        if ( match.getDate() != null )
            cv.put(DBConstants.cDATE, DateFormatFactory.getInstance().getDateFormat().format(match.getDate()));
        cv.put(DBConstants.cLASTMODIFIED, DateFormatFactory.getInstance().getDateFormat().format(new Date()));
        cv.put(DBConstants.cLASTSYNCHRONIZED, DateFormatFactory.getInstance().getDateFormat().format(new Date()));

        return cv;
    }

    @Override
    public void insert(Context context, DMatch match) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( match );

        long newRowId;
        newRowId = db.insert(DBConstants.tMATCHES, null, values);
    }

    @Override
    public void update(Context context, DMatch match) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal( match );

        values.put(DBConstants.cID, match.getId());

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString(match.getId()) };
        db.update(DBConstants.tMATCHES, values, where, projection );
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString( id ) };
        db.delete(DBConstants.tMATCHES, where, projection);

        //TODO vymazat participanty (zavolat participantDAO, at to vyresi)

    }

    @Override
    public ArrayList<DMatch> getByTournamentId(Context context, long tournamentId) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( tournamentId ) };
        Cursor cursor = db.query(DBConstants.tMATCHES, null, DBConstants.cTOURNAMENT_ID + "=?", selArgs, null, null, null);

        ArrayList<DMatch> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add( CursorParser.getInstance().parseDMatch( cursor ));
        }

        cursor.close();

        return res;
    }

    @Override
    public ArrayList<DParticipant> getParticipantsByMatchId(Context context, long matchId) {
        return null;
    }
}
