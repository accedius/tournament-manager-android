package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ICompetitionManager {

    void insert(Context context, Competition competition);
    void update(Context context, Competition competition);
    boolean delete(Context context, long id);

    Competition getById(Context context, long id);
}
