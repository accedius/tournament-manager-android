package fit.cvut.org.cz.tmlibrary.business.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat;

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
}
