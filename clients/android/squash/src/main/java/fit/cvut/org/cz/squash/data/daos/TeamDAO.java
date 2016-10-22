package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;

/**
 * Provides implementation of interface over SQLite database
 * Created by Vaclav on 14. 4. 2016.
 */
public class TeamDAO implements ITeamDAO {
    @Override
    public long insert(Context context, DTeam team) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, team.getName());
        cv.put(DBConstants.cTOURNAMENT_ID, team.getTournamentId());

        long teamId = db.insert(DBConstants.tTEAMS, null, cv);
        db.close();
        return teamId;
    }

    @Override
    public void update(Context context, DTeam team) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, team.getName());
        cv.put(DBConstants.cTOURNAMENT_ID, team.getTournamentId());

        String where = DBConstants.cID + " =?";

        db.update(DBConstants.tTEAMS, cv, where, new String[]{Long.toString(team.getId())});
        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        db.delete(DBConstants.tTEAMS, DBConstants.cID + " =?", new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public DTeam getById(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tTEAMS, DBConstants.cID), new String[]{Long.toString(id)});

        DTeam team = null;

        if (c.moveToNext())
            team = CursorParser.getInstance().parseDTeam(c);
        c.close();
        db.close();

        return team;
    }

    @Override
    public ArrayList<DTeam> getByTournamentId(Context context, long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        Cursor c = db.rawQuery(String.format("select * from %s where %s = ?", DBConstants.tTEAMS, DBConstants.cTOURNAMENT_ID), new String[]{Long.toString(tournamentId)});

        ArrayList<DTeam> teams = new ArrayList<>();

        while (c.moveToNext())
            teams.add(CursorParser.getInstance().parseDTeam(c));
        c.close();
        db.close();

        return teams;
    }
}
