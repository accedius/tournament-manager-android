package fit.cvut.org.cz.bowling.business.managers.interfaces;

import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

public interface IWinConditionManager extends IManager<Frame> {
    /**
     * @param id    of the tournament
     * @return      WinCondition entity
     */
    WinCondition getByTournamentId(long id);

    /**
     * Delete win condition entity
     * @param id    of the tournament
     */
    void deleteByTournamentId(long id);
}
