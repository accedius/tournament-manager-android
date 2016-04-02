package fit.cvut.org.cz.squash.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ICompetitionDAO;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public class CompetitionDAO implements ICompetitionDAO {


    @Override
    public void insert(Context context, DCompetition competition) {

        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase(context);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues cv = new ContentValues();
        cv.put(DBConstants.cNAME, competition.getName());
        cv.put(DBConstants.cTYPE, competition.getType());
        cv.put(DBConstants.cNOTE, competition.getNote());
        if (competition.getStartDate() != null)
            cv.put(DBConstants.cSTART, format.format(competition.getStartDate()));
        if (competition.getEndDate() != null)
            cv.put(DBConstants.cEND, format.format(competition.getEndDate()));

        db.insert(DBConstants.tCOMPETITIONS, null, cv);
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




