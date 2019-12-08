package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

public interface IFrameManager extends IManager<Frame> {

    /**
     * get all frames in a match
     * @param matchId
     * @return list of frames
     */
    List<Frame> getByMatchId (long matchId);

    /**
     * get frames in match, which are related to a participant
     * @param participantId
     * @return list of frames
     */
    List<Frame> getInMatchByParticipantId (long participantId);

    /**
     * get frames in match, which are related to a player
     * @param matchId
     * @param playerId
     * @return list of frames
     */
    List<Frame> getInMatchByPlayerId (long matchId, long playerId);

    /**
     * delete all frames and related rolls in a match
     * @param matchId
     * @return is delete successful
     */
    boolean deleteAllByMatchId (long matchId);
}
