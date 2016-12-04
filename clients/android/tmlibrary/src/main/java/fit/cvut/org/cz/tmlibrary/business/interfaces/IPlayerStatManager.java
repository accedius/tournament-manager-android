package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;

/**
 * Created by kevin on 2.12.2016.
 */
public interface IPlayerStatManager extends IManager<PlayerStat> {
    /**
     * get all players for participant
     * @param context application context
     * @param participantId id of the participant
     * @return found player stats
     */
    List<PlayerStat> getByParticipantId(Context context, long participantId);
}
