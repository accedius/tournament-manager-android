package fit.cvut.org.cz.tmlibrary.business.managers;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

/**
 * Participant Manager.
 */
abstract public class ParticipantManager extends BaseManager<Participant> implements IParticipantManager {
    @Override
    protected Class<Participant> getMyClass() {
    return Participant.class;
}

    @Override
    public Participant getByRoleAndMatchId(String role, long matchId) {
        if (role == null)
            return null;
        List<Participant> participants = getByMatchId(matchId);
        for (Participant participant : participants) {
            if (role.equals(participant.getRole())) {
                return participant;
            }
        }
        return null;
    }
}
