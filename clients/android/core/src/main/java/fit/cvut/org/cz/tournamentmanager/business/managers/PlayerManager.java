package fit.cvut.org.cz.tournamentmanager.business.managers;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.PackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.IPlayerManager;

/**
 * Player Manager - class for managing Player entity.
 */
public class PlayerManager extends BaseManager<Player> implements IPlayerManager {
    @Override
    protected Class<Player> getMyClass() {
        return Player.class;
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = super.getAll();
        PackagePlayerManager.orderPlayers(players);
        return players;
    }

    @Override
    public void insert(Player entity) {
        List<Player> emptyList = getByColumn(DBConstants.cEMAIL, entity.getEmail());
        if (!emptyList.isEmpty())
            return;
        super.insert(entity);
    }
}