package fit.cvut.org.cz.tmlibrary.data;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class CursorParser {
    private static CursorParser ourInstance = new CursorParser();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static CursorParser getInstance()
    {
        return ourInstance;
    }

    public DCompetition parseDCompetition(Cursor cursor) {
        long id;
        String uid, name, note, type, etag;
        Date startDate = null;
        Date endDate = null;
        Date lastModified = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));

        try {
            if( cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)) != null )
                startDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)));
            if( cursor.getString(cursor.getColumnIndex(DBConstants.cEND)) != null )
                endDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cEND)));
            if( cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null )
                lastModified = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        type = cursor.getString(cursor.getColumnIndex(DBConstants.cTYPE));
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        return new DCompetition(id, name, startDate, endDate, note, type, etag, uid, lastModified);
    }

    public DPlayer parseDPlayer(Cursor cursor) {
        long id;
        String uid, name, email, note, etag;
        Date lastModified = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));
        email = cursor.getString(cursor.getColumnIndex(DBConstants.cEMAIL));
        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        return new DPlayer(id, name, email, note, etag, uid, lastModified);
    }
}
