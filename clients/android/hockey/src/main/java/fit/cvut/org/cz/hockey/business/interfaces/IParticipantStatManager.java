package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 4.12.2016.
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
     * Get scored goals for specified team participant.
     * @param context application context.
     * @param participantId id of participant.
     * @return scored goals.
     */
    int getScoreByParticipantId(Context context, long participantId);
}
