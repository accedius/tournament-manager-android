package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;


public interface IParticipantStatManager extends IManager<ParticipantStat> {
    /**
     * Get all participant stats for participant.
     * @param participantId id of participant.
     * @return list of participant stats.
     */
    List<ParticipantStat> getByParticipantId(long participantId);

    /**
     * Get scored goals for specified team participant.
     * @param participantId id of participant.
     * @return scored goals.
     */
    int getScoreByParticipantId(long participantId);
}
