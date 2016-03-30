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
//        values.put(DBConstants.CID, competition.getId());
//        values.put(DBConstants.UID, competition.getUid());
//        values.put(DBConstants.CNAME, competition.getName());
//        values.put(DBConstants.cTYPE, competition.getType());
//        values.put(DBConstants.cSTART, sdf.format(competition.getStartDate()));
//        values.put(DBConstants.cEND, sdf.format(competition.getEndDate()));
//        values.put(DBConstants.cNOTE, competition.getNote());

        values.put(DBConstants.CID, 1);
        values.put(DBConstants.UID, "45862");
        values.put(DBConstants.CNAME, "MockComp");
        values.put(DBConstants.cTYPE, "Tymy");
        values.put(DBConstants.cSTART,  "2017-01-01");
        values.put(DBConstants.cEND,  "2017-01-01");
        values.put(DBConstants.cNOTE,  "----");

        long newRowId;
        newRowId = db.insert(DBConstants.tCOMPETITIONS, null, values);

        db.execSQL(String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                DBConstants.tCOMPETITIONS, 2, "90833", "MockComp2", "2017-06-01", "2017-07-01", "team", "---"));

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
