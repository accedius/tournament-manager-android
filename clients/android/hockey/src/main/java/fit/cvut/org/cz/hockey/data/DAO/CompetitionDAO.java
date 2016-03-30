package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;

import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {

    private HockeyDBHelper helper;

    @Override
    public void insert(Context context, DCompetition competition) {
        helper = new HockeyDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(DBConstants.CID, competition.getId());
//        values.put(DBConstants.UID, competition.getUid());
//        values.put(DBConstants.CNAME, competition.getName());
//        values.put(DBConstants.cTYPE, competition.getType());
//        values.put(DBConstants.cSTART, competition.getStartDate());
//        values.put(DBConstants.cEND, competition.getEndDate());

        String query = String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                DBConstants.tCOMPETITIONS, competition.getId(), competition.getUid(), competition.getName(), competition.getStartDate(), competition.getEndDate(), competition.getType(), competition.getNote());

        db.execSQL(query);
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
