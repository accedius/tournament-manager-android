package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {

    protected HockeyDBHelper helper;

    @Override
    public void insert(Context context, DCompetition competition) {
        helper = new HockeyDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues values = new ContentValues();
        //values.put(DBConstants.CID, competition.getId());
        values.put(DBConstants.UID, competition.getUid());
        values.put(DBConstants.CNAME, competition.getName());
        values.put(DBConstants.cTYPE, competition.getType());
        if ( competition.getStartDate() != null )
            values.put(DBConstants.cSTART, sdf.format(competition.getStartDate()));
        if ( competition.getEndDate() != null )
            values.put(DBConstants.cEND, sdf.format(competition.getEndDate()));
        values.put(DBConstants.cNOTE, competition.getNote());


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
        return null;
    }
}
