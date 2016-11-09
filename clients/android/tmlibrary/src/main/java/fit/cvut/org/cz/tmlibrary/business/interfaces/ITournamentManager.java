package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentManager extends IManager<Tournament> {
    /**
     * get all tournaments in competition
     * @param context application context
     * @param competitionId id of the competition
     * @return found tournaments
     */
    ArrayList<Tournament> getByCompetitionId(Context context, long competitionId);
}
