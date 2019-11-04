package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;


public interface IPlayerStatManager extends IManager<PlayerStat> {
    /**
     * get all player stats for player
     * @param playerId id of the player
     * @return found player stats
     */
    List<PlayerStat> getByPlayerId(long playerId);
    /**
     * get all player stats for participant
     * @param participantId id of the participant
     * @return found player stats
     */
    List<PlayerStat> getByParticipantId(long participantId);

    /**
     * delete all player stats for participant
     * @param participantId id of the participant
     */
    void deleteByParticipantId(long participantId);
}
