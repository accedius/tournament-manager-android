package fit.cvut.org.cz.hockey.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

/**
 * Created by kevin on 4.12.2016.
 */
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
