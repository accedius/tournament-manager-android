package fit.cvut.org.cz.bowling.business.managers;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class ParticipantManager extends fit.cvut.org.cz.tmlibrary.business.managers.ParticipantManager {
    @Override
    public List<Participant> getByMatchId(long matchId) {
        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            List<ParticipantStat> participantStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(participant.getId());
            participant.setParticipantStats(participantStats);
        }
        return new ArrayList<>(participants);
    }
}
