package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 2.12.2016.
 */
public interface IPlayerStatManager extends IManager<PlayerStat> {
    /**
     * get all player stats for participant
     * @param context application context
     * @param participantId id of the participant
     * @return found player stats
     */
    List<PlayerStat> getByParticipantId(Context context, long participantId);

    /**
     * delete all player stats for participant
     * @param context application context
     * @param participantId id of the participant
     */
    void deleteByParticipantId(Context context, long participantId);
}
