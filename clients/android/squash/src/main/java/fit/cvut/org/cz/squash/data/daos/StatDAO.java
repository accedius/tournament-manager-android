package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.squash.data.interfaces.IStatDAO;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 21. 4. 2016.
 */
public class StatDAO implements IStatDAO {

    private ContentValues convert(DStat stat){
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cCOMPETITIONID, stat.getCompetitionId());
        cv.put(DBConstants.cTOURNAMENT_ID, stat.getTournamentId());
        cv.put(DBConstants.cPARTICIPANT_ID, stat.getParticipantId());
        cv.put(DBConstants.cPLAYER_ID, stat.getPlayerId());
        cv.put(SDBConstants.cTYPE, stat.getType().toString());
        cv.put(SDBConstants.cSTATUS, stat.getStatus());
        if (stat.getValue() != -1) cv.put(SDBConstants.cVALUE, stat.getValue());
        if (stat.getNumber() != -1) cv.put(SDBConstants.cNUMBER, stat.getNumber());

        return cv;
    }

    @Override
    public void insert(Context context, DStat stat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = convert(stat);

        db.insert(SDBConstants.tSTATS, null, cv);
        db.close();
    }

    @Override
    public ArrayList<Long> getPlayerIdsForParticipant(Context context, long id) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ArrayList<Long> ids = new ArrayList<>();

        Cursor c = db.rawQuery(String.format("select %s from %s where %s = ? and %s = ?", DBConstants.cPLAYER_ID, SDBConstants.tSTATS, DBConstants.cPARTICIPANT_ID, SDBConstants.cTYPE), new String[]{Long.toString(id), StatsEnum.MATCH_PARTICIPATION.toString()});
        while (c.moveToNext())
            ids.add(c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID)));

        c.close();
        db.close();

        return ids;
    }
}
