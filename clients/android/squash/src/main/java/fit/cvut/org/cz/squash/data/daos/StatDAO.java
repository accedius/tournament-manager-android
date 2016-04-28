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
        if (stat.getPlayerId() != -1)
            cv.put(DBConstants.cPLAYER_ID, stat.getPlayerId());
        cv.put(SDBConstants.cTYPE, stat.getType().toString());
        cv.put(SDBConstants.cSTATUS, stat.getStatus());
        if (stat.getValue() != -1) cv.put(SDBConstants.cVALUE, stat.getValue());
        if (stat.getLostValue() != -1) cv.put(SDBConstants.cLOSTVALUE, stat.getLostValue());

        return cv;
    }

    private DStat parse(Cursor c){

        long playerId = -1;
        int number = -1;
        int value = -1;

        if (!c.isNull(c.getColumnIndex(DBConstants.cPLAYER_ID))) playerId = c.getLong(c.getColumnIndex(DBConstants.cPLAYER_ID));
        if (!c.isNull(c.getColumnIndex(SDBConstants.cLOSTVALUE))) number = c.getInt(c.getColumnIndex(SDBConstants.cLOSTVALUE));
        if (!c.isNull(c.getColumnIndex(SDBConstants.cVALUE))) value = c.getInt(c.getColumnIndex(SDBConstants.cVALUE));

        long id = c.getLong(c.getColumnIndex(DBConstants.cID));
        long competitionId = c.getLong(c.getColumnIndex(DBConstants.cCOMPETITIONID));
        long tournamentId = c.getLong(c.getColumnIndex(DBConstants.cTOURNAMENT_ID));
        long participantId = c.getLong(c.getColumnIndex(DBConstants.cPARTICIPANT_ID));
        StatsEnum type = StatsEnum.valueOf(c.getString(c.getColumnIndex(SDBConstants.cTYPE)));
        int status = c.getInt(c.getColumnIndex(SDBConstants.cSTATUS));

       return new DStat(id, competitionId, tournamentId, playerId, participantId, status, number, value, type);
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

    @Override
    public void delete(Context context, long participantId, StatsEnum type) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String where = String.format("%s = ? and %s =?", DBConstants.cPARTICIPANT_ID, SDBConstants.cTYPE);
        db.delete(SDBConstants.tSTATS, where, new String[] { Long.toString(participantId), type.toString()});

        db.close();
    }

    @Override
    public void update(Context context, DStat stat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = convert(stat);
        cv.put(DBConstants.cID, stat.getId());

        String where = String.format("%s = ?", DBConstants.cID);
        db.update(SDBConstants.tSTATS, cv, where, new String[]{Long.toString(stat.getId())});
        db.close();
    }


    @Override
    public void deleteByParticipant(Context context, long participantId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cPARTICIPANT_ID);
        db.delete(SDBConstants.tSTATS, where, new String[]{Long.toString(participantId)});
        db.close();
    }

    @Override
    public ArrayList<DStat> getByParticipant(Context context, long participantId, StatsEnum type) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ArrayList<DStat> stats = new ArrayList<>();

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ? and %s = ?", SDBConstants.tSTATS, DBConstants.cPARTICIPANT_ID, SDBConstants.cTYPE), new String[]{Long.toString(participantId), type.toString()});
        while (c.moveToNext())
            stats.add(parse(c));

        c.close();
        db.close();

        return stats;
    }

    @Override
    public ArrayList<DStat> getByTournament(Context context, long tournamentId, StatsEnum type) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ArrayList<DStat> stats = new ArrayList<>();

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ? and %s = ?", SDBConstants.tSTATS, DBConstants.cTOURNAMENT_ID, SDBConstants.cTYPE), new String[]{Long.toString(tournamentId), type.toString()});
        while (c.moveToNext())
            stats.add(parse(c));

        c.close();
        db.close();

        return stats;
    }

    @Override
    public ArrayList<DStat> getByCompetition(Context context, long competitionId, StatsEnum type) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ArrayList<DStat> stats = new ArrayList<>();

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ? and %s = ?", SDBConstants.tSTATS, DBConstants.cCOMPETITIONID, SDBConstants.cTYPE), new String[]{Long.toString(competitionId), type.toString()});
        while (c.moveToNext())
            stats.add(parse(c));

        c.close();
        db.close();

        return stats;
    }


}
