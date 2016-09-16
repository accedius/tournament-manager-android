package fit.cvut.org.cz.tournamentmanager.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPlayerDAO;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 7. 4. 2016.
 */
public class PlayerDAO implements IPlayerDAO {

    private ContentValues serializePlayer(DPlayer player) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, player.getName());
        cv.put(DBConstants.cEMAIL, player.getEmail());
        cv.put(DBConstants.cNOTE, player.getNote());
        cv.put(DBConstants.cLASTMODIFIED, DateFormater.getInstance().getDateTimeFormat().format(new Date()));

        return cv;
    }


    @Override
    public void insert(Context context, DPlayer player) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializePlayer(player);

        db.insert(DBConstants.tPLAYERS, null, cv);
        db.close();
    }

    @Override
    public void update(Context context, DPlayer player) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues cv = serializePlayer(player);
        cv.put(DBConstants.cID, player.getId());
        cv.put(DBConstants.cUID, player.getUid());
        cv.put(DBConstants.cETAG, player.getEtag());


        String where = String.format("%s = ?", DBConstants.cID);
        db.update(DBConstants.tPLAYERS, cv, where, new String[]{Long.toString(player.getId())});
        db.close();
    }

    @Override
    public void delete(Context context, long id) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cID);
        db.delete(DBConstants.tPLAYERS, where, new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public DPlayer getById(Context context, long id) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ?", DBConstants.tPLAYERS, DBConstants.cID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(id)});

        DPlayer player = null;

        if (c.moveToFirst())
            player = CursorParser.getInstance().parseDPlayer(c);

        c.close();

        return player;

    }

    public ArrayList<DPlayer> getAll(Context context) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s order by name collate nocase asc", DBConstants.tPLAYERS);

        Cursor c = db.rawQuery(selection, null);
        ArrayList<DPlayer> players = new ArrayList<>();

        c.moveToFirst();
        if (c.getCount() == 0)
            return players;
        do {
            players.add(CursorParser.getInstance().parseDPlayer(c));
        } while (c.moveToNext());
        c.close();

        return players;

    }
}




