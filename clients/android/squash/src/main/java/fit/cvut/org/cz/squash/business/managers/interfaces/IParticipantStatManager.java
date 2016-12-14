package fit.cvut.org.cz.squash.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

/**
 * Created by kevin on 9.12.2016.
 */

public interface IParticipantStatManager extends IManager<ParticipantStat> {
    /**
     * Get all participant stats for participant.
     * @param participantId id of participant.
     * @return list of participant stats.
     */
    List<ParticipantStat> getByParticipantId(long participantId);

    /**
     * Deletes all stats of match.
     * @param matchId id of match.
     */
    void deleteByMatchId(long matchId);
}
