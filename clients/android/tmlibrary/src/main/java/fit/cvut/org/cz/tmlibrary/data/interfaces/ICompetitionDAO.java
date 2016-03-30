package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ICompetitionDAO {

    void insert(Context context, DCompetition competition);
    void update(Context context, DCompetition competition);
    void delete(Context context, long id);

    DCompetition getById(Context context, long id);
}
