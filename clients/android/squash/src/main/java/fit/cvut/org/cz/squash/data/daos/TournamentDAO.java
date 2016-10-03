package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

/**
 * Provides implementation of interface over SQLite database
 * Created by Vaclav on 29. 3. 2016.
 */
public class TournamentDAO implements ITournamentDAO {
    private ContentValues serializeTournament(DTournament tournament) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, tournament.getName());
        cv.put(DBConstants.cNOTE, tournament.getNote());
        if (tournament.getStartDate() != null)
            cv.put(DBConstants.cSTART, dateFormat.format(tournament.getStartDate()));
        if (tournament.getEndDate() != null)
            cv.put(DBConstants.cEND, dateFormat.format(tournament.getEndDate()));
        cv.put(DBConstants.cLASTMODIFIED, dateTimeFormat.format(new Date()));
        cv.put(DBConstants.cCOMPETITIONID, tournament.getCompetitionId());

        return cv;
    }

    @Override
    public long insert(Context context, DTournament tournament) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializeTournament(tournament);

        long id = db.insert(DBConstants.tTOURNAMENTS, null, cv);
        db.close();
        return id;
    }

    @Override
    public void update(Context context, DTournament tournament) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializeTournament(tournament);
        cv.put(DBConstants.cID, tournament.getId());
        cv.put(DBConstants.cUID, tournament.getUid());
        cv.put(DBConstants.cETAG, tournament.getEtag());

        String where = String.format("%s = ?", DBConstants.cID);
        db.update(DBConstants.tTOURNAMENTS, cv, where, new String[]{Long.toString(tournament.getId())});
        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cID);
        db.delete(DBConstants.tTOURNAMENTS, where, new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public DTournament getById(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ?", DBConstants.tTOURNAMENTS, DBConstants.cID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(id)});

        DTournament tournament = null;

        if (c.moveToFirst())
            tournament = CursorParser.getInstance().parseDTournament(c);

        c.close();
        db.close();

        return tournament;
    }

    @Override
    public ArrayList<DTournament> getByCompetitionId(Context context, long competitionId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ? order by %s", DBConstants.tTOURNAMENTS, DBConstants.cCOMPETITIONID, DBConstants.cSTART + " DESC, " + DBConstants.cEND + " DESC");

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(competitionId)});

        ArrayList<DTournament> tournaments = new ArrayList<>();

        while (c.moveToNext())
            tournaments.add(CursorParser.getInstance().parseDTournament(c));

        c.close();
        db.close();

        return tournaments;
    }
}

