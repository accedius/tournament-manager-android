package fit.cvut.org.cz.squash.data.daos;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;

/**
 * Created by Vaclav on 21. 4. 2016.
 */
public class ParticipantDAO implements IParticipantDAO {
    @Override
    public long insert(Context context, DParticipant participant, boolean playersToo) {
        return 0;
    }

    @Override
    public void update(Context context, DParticipant participant, boolean playersToo) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public ArrayList<DParticipant> getParticipantsByMatchId(Context context, long matchId) {
        return null;
    }
}
