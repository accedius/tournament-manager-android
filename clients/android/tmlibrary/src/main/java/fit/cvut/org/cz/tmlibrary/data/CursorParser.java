package fit.cvut.org.cz.tmlibrary.data;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class CursorParser {
    private static CursorParser ourInstance = new CursorParser();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
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

    public DTournament parseDTournament(Cursor cursor) {
        long id, competitionId;
        String uid, name, note, etag;
        Date startDate = null;
        Date endDate = null;
        Date lastModified = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));
        competitionId = cursor.getLong(cursor.getColumnIndex(DBConstants.cCOMPETITIONID));

        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)) != null)
                startDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cSTART)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cEND)) != null)
                endDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cEND)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        return new DTournament(id, name, startDate, endDate, note, etag, uid, lastModified, competitionId);
    }

    public DTeam parseDTeam(Cursor c){
        return new DTeam(
                c.getLong(c.getColumnIndex(DBConstants.cID)),
                c.getLong(c.getColumnIndex(DBConstants.cTOURNAMENT_ID)),
                c.getString(c.getColumnIndex(DBConstants.cNAME)));
    }
}
