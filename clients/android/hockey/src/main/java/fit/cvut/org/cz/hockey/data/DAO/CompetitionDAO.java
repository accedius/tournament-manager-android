package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {
    private static SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
    private static SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

    private ContentValues toContVal(DCompetition competition) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cUID, competition.getUid());
        cv.put(DBConstants.cETAG, competition.getEtag());
        cv.put(DBConstants.cNAME, competition.getName());
        cv.put(DBConstants.cTYPE, competition.getType());
        if (competition.getStartDate() != null)
            cv.put(DBConstants.cSTART, dateFormat.format(competition.getStartDate()));
        if (competition.getEndDate() != null)
            cv.put(DBConstants.cEND, dateFormat.format(competition.getEndDate()));
        cv.put(DBConstants.cNOTE, competition.getNote());
        cv.put(DBConstants.cLASTMODIFIED, dateTimeFormat.format(new Date()));
        if (competition.getLastSynchronized() != null)
            cv.put(DBConstants.cLASTSYNCHRONIZED, dateTimeFormat.format(competition.getLastSynchronized()));

        return cv;
    }

    @Override
    public long insert(Context context, DCompetition competition) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(competition);

        long newRowId = db.insert(DBConstants.tCOMPETITIONS, null, values);

        db.close();
        return newRowId;
    }

    @Override
    public void update(Context context, DCompetition competition) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal(competition);

        //values.put(DBConstants.cID, competition.getId());

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(competition.getId()) };
        db.update(DBConstants.tCOMPETITIONS, values, where, projection);

        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ?", DBConstants.cID);
        String[] projection = new String[]{ Long.toString(id) };
        db.delete(DBConstants.tCOMPETITIONS, where, projection);

        db.close();
    }

    @Override
    public DCompetition getById(Context context, long id) {
        String[] selArgs = { String.valueOf(id) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor cursor = db.query(DBConstants.tCOMPETITIONS, null, DBConstants.cID + "=?", selArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0)
            return null;
        DCompetition res = CursorParser.getInstance().parseDCompetition(cursor);
        
        cursor.close();
        db.close();

        return res;
    }
}
