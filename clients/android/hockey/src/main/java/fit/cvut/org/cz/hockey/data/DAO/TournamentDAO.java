package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentDAO implements ITournamentDAO {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private ContentValues toContVal(DTournament tournament)
    {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cUID, tournament.getUid());
        cv.put(DBConstants.cETAG, tournament.getEtag());
        cv.put(DBConstants.cNAME, tournament.getName());
        if ( tournament.getStartDate() != null )
            cv.put(DBConstants.cSTART, sdf.format(tournament.getStartDate()));
        if ( tournament.getEndDate() != null )
            cv.put(DBConstants.cEND, sdf.format(tournament.getEndDate()));
        cv.put(DBConstants.cNOTE, tournament.getNote());
        cv.put(DBConstants.cLASTMODIFIED, sdf.format(new Date()));
        cv.put(DBConstants.cCOMPETITIONID, tournament.getCompetitionId());

        return cv;
    }

    @Override
    public void insert(Context context, DTournament tournament) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( tournament );

        long newRowId;
        newRowId = db.insert(DBConstants.tTOURNAMENTS, null, values);

    }

    @Override
    public void update(Context context, DTournament tournament) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal( tournament );

        values.put(DBConstants.cID, tournament.getId());

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString(tournament.getId()) };
        db.update(DBConstants.tTOURNAMENTS, values, where, projection );
    }

    @Override
    public void delete(Context context, long id) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString( id ) };
        db.delete(DBConstants.tTOURNAMENTS, where, projection);
    }

    @Override
    public DTournament getById(Context context, long id) {
        String[] selArgs = { String.valueOf( id ) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        Cursor cursor = db.query( DBConstants.tTOURNAMENTS, null, DBConstants.cID + "=?", selArgs, null, null, null );
        cursor.moveToFirst();
        if( cursor.getCount() <= 0 )
            return null;
        DTournament res = CursorParser.getInstance().parseDTournament(cursor);

        cursor.close();

        return res;
    }

    @Override
    public ArrayList<DTournament> getByCompetitionId(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( competitionId ) };
        Cursor cursor = db.query(DBConstants.tTOURNAMENTS, null, DBConstants.cCOMPETITIONID + "=?", selArgs, null, null, null);

        ArrayList<DTournament> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            res.add( CursorParser.getInstance().parseDTournament( cursor ));
        }

        cursor.close();

        return res;

    }
}
