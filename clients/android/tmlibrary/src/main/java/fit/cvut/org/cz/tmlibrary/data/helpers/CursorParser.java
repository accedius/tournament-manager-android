package fit.cvut.org.cz.tmlibrary.data.helpers;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class CursorParser {
    private static SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

    public static Player parsePlayer(Cursor cursor) {
        long id;
        String uid, name, email, note, etag;
        Date lastModified = null;
        Date lastSynchronized = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));
        email = cursor.getString(cursor.getColumnIndex(DBConstants.cEMAIL));
        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));
        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)) != null)
                lastSynchronized = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Player p = new Player(id, uid, name, email, note);
        p.setEtag(etag);
        p.setLastModified(lastModified);
        p.setLastSynchronized(lastSynchronized);
        return p;
    }
}
