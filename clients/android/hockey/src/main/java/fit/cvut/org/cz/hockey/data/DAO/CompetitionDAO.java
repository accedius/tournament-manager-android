package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {



    @Override
    public void insert(Context context, DCompetition competition) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues values = new ContentValues();
//        values.put(DBConstants.cID, competition.getId());
//        values.put(DBConstants.cUID, competition.getUid());
//        values.put(DBConstants.cNAME, competition.getName());
        values.put(DBConstants.cTYPE, competition.getType());
        if ( competition.getStartDate() != null )
            values.put(DBConstants.cSTART, sdf.format(competition.getStartDate()));
        if ( competition.getEndDate() != null )
            values.put(DBConstants.cEND, sdf.format(competition.getEndDate()));
        values.put(DBConstants.cNOTE, competition.getNote());

        values.put(DBConstants.cID, 1);
        values.put(DBConstants.cUID, "45862");
        values.put(DBConstants.cNAME, "MockComp");

        long newRowId;
        newRowId = db.insert(DBConstants.tCOMPETITIONS, null, values);

    }

    @Override
    public void update(Context context, DCompetition competition) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public DCompetition getById(Context context, long id) {
        String[] selArgs = { String.valueOf( id ) };
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );
        Cursor cursor = db.query( DBConstants.tCOMPETITIONS, null, DBConstants.CID + "=?", selArgs, null, null, null );
        cursor.moveToFirst();
        if( cursor.getCount() <= 0 )
            return null;
        return new DCompetition( cursor );
    }
}
