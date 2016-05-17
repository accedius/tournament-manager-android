package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.squash.data.entities.DPointConfig;
import fit.cvut.org.cz.squash.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Provides implementation of interface over SQLite database
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigDAO implements IPointConfigDAO {

    private ContentValues convert(DPointConfig cfg){

        ContentValues cv = new ContentValues();
        cv.put(SDBConstants.cWIN, cfg.getWin());
        cv.put(SDBConstants.cLOSS, cfg.getLoss());
        cv.put(SDBConstants.cDRAW, cfg.getDraw());
        return cv;
    }

    @Override
    public void insert(Context context, long id) {

        ContentValues cv = convert(new DPointConfig(id, 3, 1, 0));
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        cv.put(DBConstants.cTOURNAMENT_ID, id);

        db.insert(SDBConstants.tPOINT_CONFIG, null, cv);
        db.close();

    }

    @Override
    public void update(Context context, DPointConfig cfg) {
        ContentValues cv = convert(cfg);
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        db.update(SDBConstants.tPOINT_CONFIG, cv, DBConstants.cTOURNAMENT_ID + "= ?", new String[]{Long.toString(cfg.getTournamentId())});
        db.close();
    }

    @Override
    public void delete(Context context, long id) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        db.delete(SDBConstants.tPOINT_CONFIG, DBConstants.cTOURNAMENT_ID + "= ?", new String[]{Long.toString(id)});
        db.close();
    }

    @Override
    public DPointConfig getById(Context context, long id) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        String selection = String.format("select * from %s where %s = ?", SDBConstants.tPOINT_CONFIG, DBConstants.cTOURNAMENT_ID);

        Cursor c = db.rawQuery(selection, new String[]{Long.toString(id)});

        DPointConfig cfg = null;

        if (c.moveToFirst()) {
            cfg = new DPointConfig(c.getLong(c.getColumnIndex(DBConstants.cTOURNAMENT_ID)),
                                c.getInt(c.getColumnIndex(SDBConstants.cWIN)),
                                c.getInt(c.getColumnIndex(SDBConstants.cDRAW)),
                                c.getInt(c.getColumnIndex(SDBConstants.cLOSS)));
        }

        c.close();
        db.close();

        return cfg;

    }
}
