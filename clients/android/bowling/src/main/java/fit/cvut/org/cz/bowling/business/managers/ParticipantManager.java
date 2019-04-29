package fit.cvut.org.cz.bowling.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

public class ParticipantManager extends fit.cvut.org.cz.tmlibrary.business.managers.ParticipantManager {
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
}
