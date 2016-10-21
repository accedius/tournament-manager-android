package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.hockey.data.interfaces.IMatchStatisticsDAO;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class MatchStatisticsDAO implements IMatchStatisticsDAO {
    private ContentValues toContVal(DMatchStat stat) {
        ContentValues values = new ContentValues();
        values.put(HockeyDBConstants.cOVERTIME, stat.isOvertime());
        values.put(HockeyDBConstants.cSHOOTOUTS, stat.isShootouts());
        values.put(DBConstants.cMATCH_ID, stat.getMatchId());
        return values;
    }

    @Override
    public long createStatsForMatch(Context context, DMatchStat stat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues values = toContVal(stat);

        long newRowId = db.insert(HockeyDBConstants.tMATCH_SCORE, null, values);
        db.close();
        return newRowId;
    }


    public DMatchStat getByMatchId(Context context, long matchId) {
        String[] selArgs = { String.valueOf(matchId) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        Cursor cursor = db.query(HockeyDBConstants.tMATCH_SCORE, null, DBConstants.cMATCH_ID + "=?", selArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0)
            return null;
        DMatchStat res = new DMatchStat();
        boolean ot, so;
        ot = !(0 == cursor.getInt(cursor.getColumnIndex(HockeyDBConstants.cOVERTIME)));
        so = !(0 == cursor.getInt(cursor.getColumnIndex(HockeyDBConstants.cSHOOTOUTS)));
        res.setMatchId(matchId);
        res.setOvertime(ot);
        res.setShootouts(so);

        cursor.close();
        db.close();

        return res;
    }

    public void update(Context context, DMatchStat matchStat) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        ContentValues values = toContVal(matchStat);
        String where = String.format("%s = ?", DBConstants.cMATCH_ID);
        String[] projection = new String[]{ Long.toString(matchStat.getMatchId()) };
        db.update(HockeyDBConstants.tMATCH_SCORE, values, where, projection);
        db.close();
    }

    public void delete(Context context, long matchId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String where = String.format("%s = ?", DBConstants.cMATCH_ID);
        String[] projection = new String[]{ Long.toString(matchId) };
        db.delete(HockeyDBConstants.tMATCH_SCORE, where, projection);
        db.close();
    }

}
