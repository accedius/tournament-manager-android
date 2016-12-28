package fit.cvut.org.cz.tmlibrary.data.helpers;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class CursorParser {
    private static CursorParser ourInstance = new CursorParser();
    private static SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
    private static SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

    public static CursorParser getInstance() {
        return ourInstance;
    }

    public Competition parseCompetition(Cursor cursor) {
        long id;
        int type;
        String uid, name, note, etag;
        Date startDate = null;
        Date endDate = null;
        Date lastModified = null;
        Date lastSynchronized = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));

        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)) != null)
                startDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cEND)) != null)
                endDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cEND)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)) != null)
                lastSynchronized = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        type = cursor.getInt(cursor.getColumnIndex(DBConstants.cTYPE));
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        CompetitionType t = CompetitionTypes.competitionTypes()[type];
        Competition c = new Competition(id, uid, name, startDate, endDate, note, t);
        c.setEtag(etag);
        c.setLastModified(lastModified);
        c.setLastSynchronized(lastSynchronized);
        return c;
    }

    public Player parsePlayer(Cursor cursor) {
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
