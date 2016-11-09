package fit.cvut.org.cz.tmlibrary.data;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

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

    public Tournament parseTournament(Cursor cursor) {
        long id, competitionId;
        String uid, name, note, etag;
        Date startDate = null;
        Date endDate = null;
        Date lastModified = null;
        Date lastSynchronized = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        name = cursor.getString(cursor.getColumnIndex(DBConstants.cNAME));
        competitionId = cursor.getLong(cursor.getColumnIndex(DBConstants.cCOMPETITION_ID));

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
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        Tournament t = new Tournament(id, uid, name, startDate, endDate, note);
        t.setCompetitionId(competitionId);
        t.setEtag(etag);
        t.setLastModified(lastModified);
        t.setLastSynchronized(lastSynchronized);
        return t;
    }

    public Team parseTeam(Cursor cursor) {
        String uid, etag;
        Date lastModified = null;
        Date lastSynchronized = null;

        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)) != null)
                lastSynchronized = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        Team t = new Team(
                cursor.getLong(cursor.getColumnIndex(DBConstants.cID)),
                cursor.getLong(cursor.getColumnIndex(DBConstants.cTOURNAMENT_ID)),
                cursor.getString(cursor.getColumnIndex(DBConstants.cNAME)));
        t.setEtag(etag);
        t.setUid(uid);
        t.setLastModified(lastModified);
        t.setLastSynchronized(lastSynchronized);
        return t;
    }

    public DMatch parseDMatch(Cursor cursor) {
        long id, tournamentId;
        int period, round;
        String uid, name, note, etag;
        Date date = null;
        Date lastModified = null;
        Date lastSynchronized = null;
        boolean played;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        note = cursor.getString(cursor.getColumnIndex(DBConstants.cNOTE));
        tournamentId = cursor.getLong(cursor.getColumnIndex(DBConstants.cTOURNAMENT_ID));
        period = cursor.getInt(cursor.getColumnIndex(DBConstants.cPERIOD));
        round = cursor.getInt(cursor.getColumnIndex(DBConstants.cROUND));
        played = !(0 == cursor.getInt(cursor.getColumnIndex(DBConstants.cPLAYED)));

        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cDATE)) != null)
                date = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cDATE)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)) != null)
                lastSynchronized = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        return new DMatch(id, tournamentId, period, round, date, note, played, etag, uid, lastModified, lastSynchronized);
    }

    public DParticipant parseDParticipant(Cursor cursor) {
        long id, teamId = -1, matchId;
        String uid, role, etag;
        Date lastModified = null;
        Date lastSynchronized = null;

        id = cursor.getInt(cursor.getColumnIndex(DBConstants.cID));
        uid = cursor.getString(cursor.getColumnIndex(DBConstants.cUID));
        role = cursor.getString(cursor.getColumnIndex(DBConstants.cROLE));
        if (!cursor.isNull(cursor.getColumnIndex(DBConstants.cTEAM_ID)))
            teamId = cursor.getLong(cursor.getColumnIndex(DBConstants.cTEAM_ID));
        matchId = cursor.getLong(cursor.getColumnIndex(DBConstants.cMATCH_ID));

        try {
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)) != null)
                lastModified = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTMODIFIED)));
            if (cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)) != null)
                lastSynchronized = dateTimeFormat.parse(cursor.getString(cursor.getColumnIndex(DBConstants.cLASTSYNCHRONIZED)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        etag = cursor.getString(cursor.getColumnIndex(DBConstants.cETAG));

        return new DParticipant(id, teamId, matchId, role, etag, uid, lastModified, lastSynchronized);
    }

    public DStat parseDStat(Cursor cursor) {
        long id, participantId, playerId, tournamentId, competitionId;
        String value, statsEnumId;

        id = cursor.getLong(cursor.getColumnIndex(DBConstants.cID));
        participantId = cursor.getLong(cursor.getColumnIndex(DBConstants.cPARTICIPANT_ID));
        playerId = cursor.getLong(cursor.getColumnIndex(DBConstants.cPLAYER_ID));
        statsEnumId = cursor.getString(cursor.getColumnIndex(DBConstants.cSTATS_ENUM_ID));
        tournamentId = cursor.getLong(cursor.getColumnIndex(DBConstants.cTOURNAMENT_ID));
        competitionId = cursor.getLong(cursor.getColumnIndex(DBConstants.cCOMPETITION_ID));
        value = cursor.getString(cursor.getColumnIndex(DBConstants.cVALUE));

        return new DStat(id, playerId, participantId, statsEnumId, tournamentId, competitionId, value);
    }
}
