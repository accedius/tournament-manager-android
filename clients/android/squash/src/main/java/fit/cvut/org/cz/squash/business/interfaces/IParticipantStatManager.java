package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.squash.business.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 9.12.2016.
 */

public interface IParticipantStatManager extends IManager<ParticipantStat> {
    /**
     * Get all participant stats for participant.
     * @param context application context
     * @param participantId id of participant.
     * @return list of participant stats.
     */
    List<ParticipantStat> getByParticipantId(Context context, long participantId);

    /**
     * Deletes all stats of match.
     * @param context application context.
     * @param matchId id of match.
     */
    void deleteByMatchId(Context context, long matchId);
}
