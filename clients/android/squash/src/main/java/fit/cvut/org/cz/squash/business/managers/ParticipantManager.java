package fit.cvut.org.cz.squash.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.TManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public class ParticipantManager extends TManager<Participant> implements IParticipantManager {
    @Override
    protected Class<Participant> getMyClass() {
        return Participant.class;
    }

    @Override
    public List<Participant> getByMatchId(long matchId) {
        try {
            List<Participant> participants = managerFactory.getDaoFactory().getMyDao(Participant.class).queryForEq(DBConstants.cMATCH_ID, matchId);
            for (Participant participant : participants) {
                List<ParticipantStat> participantStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(participant.getId());
                participant.setParticipantStats(participantStats);
            }
            return new ArrayList<>(participants);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
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
