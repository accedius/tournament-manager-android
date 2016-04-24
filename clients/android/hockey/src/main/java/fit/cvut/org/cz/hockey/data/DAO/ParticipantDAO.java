package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;

/**
 * Created by atgot_000 on 18. 4. 2016.
 */
public class ParticipantDAO implements IParticipantDAO {

    private ContentValues toContVal(DParticipant participant)
    {
        ContentValues cv = new ContentValues();
        //cv.put(DBConstants.cID, participant.getId());
        cv.put(DBConstants.cUID, participant.getUid());
        cv.put(DBConstants.cETAG, participant.getEtag());
        cv.put(DBConstants.cTEAM_ID, participant.getTeamId());
        cv.put(DBConstants.cMATCH_ID, participant.getMatchId());
        cv.put(DBConstants.cROLE, participant.getRole());
        if ( participant.getLastSynchronized() != null )
            cv.put(DBConstants.cLASTSYNCHRONIZED, DateFormatFactory.getInstance().getDateFormat().format(participant.getLastSynchronized()));
        cv.put(DBConstants.cLASTMODIFIED, DateFormatFactory.getInstance().getDateFormat().format(new Date()));

        return cv;
    }


    @Override
    public long insert(Context context, DParticipant participant) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( participant );

        long newRowId;
        newRowId = db.insert(DBConstants.tPARTICIPANTS, null, values);

        return newRowId;
    }

    @Override
    public void update(Context context, DParticipant participant) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( participant );

        values.put(DBConstants.cID, participant.getId());

        String where = String.format( "%s = ?", DBConstants.cID );
        String[] projection = new String[]{ Long.toString(participant.getId()) };
        db.update(DBConstants.tPARTICIPANTS, values, where, projection );
    }

    @Override
    public void delete(Context context, long id) {
        //TODO nebo ne todo? --> ANO, protoze delete matche
    }

    @Override
    public ArrayList<DParticipant> getParticipantsByMatchId(Context context, long matchId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        String[] selArgs = { String.valueOf( matchId ) };
        Cursor cursor = db.query(DBConstants.tPARTICIPANTS, null, DBConstants.cMATCH_ID + "=?", selArgs, null, null, null);

        ArrayList<DParticipant> res = new ArrayList<>();

        while (cursor.moveToNext())
        {
            DParticipant dp = CursorParser.getInstance().parseDParticipant( cursor );
            res.add( dp );
        }

        cursor.close();

        return res;
    }

    @Override
    public DParticipant getById(Context context, long id) {
        String[] selArgs = { String.valueOf( id ) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        Cursor cursor = db.query( DBConstants.tPARTICIPANTS, null, DBConstants.cID + "=?", selArgs, null, null, null );
        cursor.moveToFirst();
        if( cursor.getCount() <= 0 )
            return null;
        DParticipant res = CursorParser.getInstance().parseDParticipant( cursor );

        cursor.close();

        return res;
    }
}
