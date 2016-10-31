package fit.cvut.org.cz.tournamentmanager.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DSetting;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 23.10.2016.
 */
public class SettingDAO {
    private ContentValues serializeSetting(DSetting setting) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cPACKAGE_NAME, setting.getPackageName());
        cv.put(DBConstants.cSPORT_NAME, setting.getSportName());
        return cv;
    }

    public void insert(Context context, DSetting setting) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues cv = serializeSetting(setting);
        db.insert(DBConstants.tSETTINGS, null, cv);
        db.close();
    }

    public void delete(Context context, String packageName, String sportName) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ? and %s = ?", DBConstants.cPACKAGE_NAME, DBConstants.cSPORT_NAME);
        db.delete(DBConstants.tSETTINGS, where, new String[]{packageName, sportName});
        db.close();
    }

    public void deleteAll(Context context) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        db.delete(DBConstants.tSETTINGS, null, null);
        db.close();
    }

    public DSetting getByPackageSport(Context context, String packageName, String sportName) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String selection = String.format("select * from %s where %s = ? and %s = ?", DBConstants.tSETTINGS, DBConstants.cPACKAGE_NAME, DBConstants.cSPORT_NAME);
        Cursor c = db.rawQuery(selection, new String[]{packageName, sportName});
        DSetting setting = null;

        if (c.moveToFirst())
            setting = CursorParser.getInstance().parseDSetting(c);
        c.close();
        return setting;
    }

    public ArrayList<DSetting> getAll(Context context) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String selection = String.format("select * from %s order by package_name collate nocase asc, sport_name collate nocase asc", DBConstants.tSETTINGS);

        Cursor c = db.rawQuery(selection, null);
        ArrayList<DSetting> settings = new ArrayList<>();

        c.moveToFirst();
        if (c.getCount() == 0) {
            db.close();
            return settings;
        }
        do {
            settings.add(CursorParser.getInstance().parseDSetting(c));
        } while (c.moveToNext());
        c.close();

        return settings;
    }
}
