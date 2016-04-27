package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;

/**
 * Created by Vaclav on 21. 4. 2016.
 */
public class ParticipantDAO implements IParticipantDAO {

    private ContentValues convert(DParticipant participant){
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cROLE, participant.getRole());
        cv.put(DBConstants.cMATCH_ID, participant.getMatchId());
        if (participant.getTeamId() != -1) cv.put(DBConstants.cTEAM_ID, participant.getTeamId());
        return cv;
    }

    @Override
    public long insert(Context context, DParticipant participant) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = convert(participant);

        long id = db.insert(DBConstants.tPARTICIPANTS, null, cv);
        db.close();

        return id;
    }

    @Override
    public void update(Context context, DParticipant participant) {

    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cID);
        db.delete(DBConstants.tPARTICIPANTS, where, new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public ArrayList<DParticipant> getParticipantsByMatchId(Context context, long matchId) {

        ArrayList<DParticipant> participants = new ArrayList<>();

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPARTICIPANTS, DBConstants.cMATCH_ID), new String[]{Long.toString(matchId)});

        while (c.moveToNext())
            participants.add(CursorParser.getInstance().parseDParticipant(c));
        c.close();
        db.close();

        return participants;
    }

    @Override
    public DParticipant getById(Context context, long id) {
        DParticipant participant = null;

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tPARTICIPANTS, DBConstants.cID), new String[]{Long.toString(id)});

        if (c.moveToNext())
            participant = CursorParser.getInstance().parseDParticipant(c);
        c.close();
        db.close();

        return participant;
    }
}
