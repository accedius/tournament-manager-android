package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;
/**
 * Provides implementation of interface over SQLite database
 * Created by Vaclav on 29. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {
    SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
    SimpleDateFormat dateTimeFormat = DateFormatter.getInstance().getDBDateTimeFormat();

    private ContentValues serializeCompetition(DCompetition competition) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, competition.getName());
        cv.put(DBConstants.cTYPE, competition.getType());
        cv.put(DBConstants.cNOTE, competition.getNote());
        if (competition.getStartDate() != null)
            cv.put(DBConstants.cSTART, dateFormat.format(competition.getStartDate()));
        if (competition.getEndDate() != null)
            cv.put(DBConstants.cEND, dateFormat.format(competition.getEndDate()));
        cv.put(DBConstants.cLASTMODIFIED, dateTimeFormat.format(new Date()));

        return cv;
    }

    @Override
    public long insert(Context context, DCompetition competition) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializeCompetition(competition);

        long competitionId = db.insert(DBConstants.tCOMPETITIONS, null, cv);
        db.close();
        return competitionId;
    }

    @Override
    public void update(Context context, DCompetition competition) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializeCompetition(competition);
        cv.put(DBConstants.cID, competition.getId());
        cv.put(DBConstants.cUID, competition.getUid());
        cv.put(DBConstants.cETAG, competition.getEtag());

        String where = String.format("%s = ?", DBConstants.cID);
        db.update(DBConstants.tCOMPETITIONS, cv, where, new String[]{Long.toString(competition.getId())});
        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cID);
        db.delete(DBConstants.tCOMPETITIONS, where, new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public DCompetition getById(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ?", DBConstants.tCOMPETITIONS, DBConstants.cID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(id)});

        DCompetition competition = null;

        if (c.moveToFirst())
            competition = CursorParser.getInstance().parseDCompetition(c);

        c.close();
        db.close();

        return competition;
    }
}

