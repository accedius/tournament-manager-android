package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.interfaces.IParticipantManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public class ParticipantManager implements IParticipantManager {
    @Override
    public long getTeamIdForMatchParticipant(Context context, long matchId, String role) {

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        for (DParticipant p : participants){
            if (p.getRole().equals(role)) return p.getTeamId();
        }

        return -1;
    }
}
