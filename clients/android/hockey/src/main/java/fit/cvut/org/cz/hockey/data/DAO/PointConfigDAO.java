package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;
import fit.cvut.org.cz.hockey.data.interfaces.IPointConfigDAO;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class PointConfigDAO implements IPointConfigDAO {

    private ContentValues toContVal( DPointConfiguration dp )
    {
        ContentValues cv = new ContentValues();
        cv.put(HockeyDBConstants.cNTW, dp.ntW );
        cv.put(HockeyDBConstants.cNTD, dp.ntD );
        cv.put(HockeyDBConstants.cNTL, dp.ntL );

        cv.put(HockeyDBConstants.cOTW, dp.otW );
        cv.put(HockeyDBConstants.cOTD, dp.otD );
        cv.put(HockeyDBConstants.cOTL, dp.otL );

        cv.put(HockeyDBConstants.cSOW, dp.soW );
        cv.put(HockeyDBConstants.cSOL, dp.soL );


        return cv;
    }

    private DPointConfiguration parseDPfromCursor( Cursor cursor )
    {
        Long ntW, ntD, ntL, otW, otD, otL, soW, soL;
        ntW = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cNTW));
        ntD = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cNTD));
        ntL = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cNTL));

        otW = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cOTW));
        otD = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cOTD));
        otL = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cOTL));

        soW = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cSOW));
        soL = cursor.getLong(cursor.getColumnIndex(HockeyDBConstants.cSOL));

        DPointConfiguration dp = new DPointConfiguration( ntW, ntD, ntL, otW, otD, otL, soW, soL );
        return dp;
    }

    @Override
    public void update(Context context, DPointConfiguration dPointConfiguration, Long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        ContentValues values = toContVal( dPointConfiguration );
        values.put(HockeyDBConstants.cTOURNAMENTID, tournamentId);

        String where = String.format( "%s = ?", HockeyDBConstants.cTOURNAMENTID );
        String[] projection = new String[]{ Long.toString(tournamentId) };
        db.update(HockeyDBConstants.tCONFIGURATIONS, values, where, projection );
    }

    @Override
    public DPointConfiguration getByTournamentId(Context context, Long tournamentId) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);
        String[] selArgs = { Long.toString(tournamentId) };
        Cursor cursor = db.query(HockeyDBConstants.tCONFIGURATIONS, null, HockeyDBConstants.cTOURNAMENTID + "=?", selArgs, null, null, null);

        cursor.moveToFirst();
        if( cursor.getCount() <= 0 )
            return null;

        DPointConfiguration res = parseDPfromCursor( cursor );

        return res;
    }
}
