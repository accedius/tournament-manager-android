package fit.cvut.org.cz.hockey.data.DAO;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchDAO implements IMatchDAO {
    @Override
    public void insert(Context context, DMatch match) {

    }

    @Override
    public void update(Context context, DMatch match) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public ArrayList<DMatch> getByTournamentId(Context context, long tournamentId) {
        return null;
    }

    @Override
    public ArrayList<DParticipant> getParticipantsByMatchId(Context context, long matchId) {
        return null;
    }
}
