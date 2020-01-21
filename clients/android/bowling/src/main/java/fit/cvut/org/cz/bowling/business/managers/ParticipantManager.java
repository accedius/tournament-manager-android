package fit.cvut.org.cz.bowling.business.managers;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class ParticipantManager extends fit.cvut.org.cz.tmlibrary.business.managers.ParticipantManager implements IParticipantManager {
    @Override
    public List<Participant> getByMatchId(long matchId) {
        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            long participantId = participant.getId();
            IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantId(participantId);
            participant.setParticipantStats(participantStats);

            /*IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
            List<PlayerStat> playerStats = playerStatManager.getByParticipantId(participantId);
            participant.setPlayerStats(playerStats);*/
        }
        return new ArrayList<>(participants);
    }

    @Override
    public List<Participant> getByMatchIdWithAllContents(long matchId) {
        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            long participantId = participant.getId();
            IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantIdWithAllContents(participantId);
            participant.setParticipantStats(participantStats);

            IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
            List<PlayerStat> playerStats = playerStatManager.getByParticipantId(participantId);
            participant.setPlayerStats(playerStats);
        }
        return new ArrayList<>(participants);
    }
}
