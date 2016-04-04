package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentDAO {

    void insert(Context context, DTournament tournament);
    void update(Context context, DTournament tournament);
    void delete(Context context, long id);

    DTournament getById(Context context, long id);
    ArrayList<DTournament> getByCompetitionId(Context context, long competitionId);
}
