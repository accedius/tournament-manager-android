package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

public interface IRollManager extends IManager<Roll> {

    /**
     * get rolls in match, which are related to a frame
     * @param matchId
     * @param frameId
     * @return list of rolls
     */
    List<Roll> getByFrameId (long matchId, long frameId);
}
