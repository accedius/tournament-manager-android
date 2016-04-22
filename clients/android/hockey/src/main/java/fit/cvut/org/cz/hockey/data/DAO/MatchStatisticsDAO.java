package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class MatchStatisticsDAO {

    private ContentValues toContVal( DMatchStat stat )
    {
        ContentValues values = new ContentValues();
        values.put(HockeyDBConstants.cOVERTIME, stat.isOvertime());
        values.put(HockeyDBConstants.cSHOOTOUTS, stat.isShootouts());
        values.put(DBConstants.cMATCH_ID, stat.getMatchId());
        return values;
    }

    public long createStatsForMatch( Context context, DMatchStat stat)
    {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( stat );

        long newRowId;
        newRowId = db.insert(HockeyDBConstants.tMATCH_SCORE, null, values);


        return newRowId;
    }



}
