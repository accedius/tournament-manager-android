package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public interface IMatchDAO {

    void insert(Context context, DMatch match);
    void update(Context context, DMatch match);
    void delete(Context context, long id);

    ArrayList<DMatch> getByTournamentId( Context context, long tournamentId );

}
