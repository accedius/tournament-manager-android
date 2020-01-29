package fit.cvut.org.cz.bowling.business.managers;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;

public class WinConditionManager extends BaseManager<WinCondition> implements IWinConditionManager {
    @Override
    public WinCondition getByTournamentId(long id) {
        return getById(id);
    }

    @Override
    public void deleteByTournamentId(long id) {
        delete(id);
    }

    @Override
    protected Class<WinCondition> getMyClass() {
        return WinCondition.class;
    }
}
