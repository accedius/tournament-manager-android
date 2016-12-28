package fit.cvut.org.cz.tournamentmanager.business.managers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.IPlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager extends BaseManager<Player> implements IPlayerManager {
    @Override
    protected Class<Player> getMyClass() {
        return Player.class;
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = super.getAll();
        orderPlayers(players);
        return players;
    }

    private void orderPlayers(List<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player lp, Player rp) {
                if (!rp.getName().equals(lp.getName()))
                    return lp.getName().compareTo(rp.getName());
                return lp.getEmail().compareTo(rp.getEmail());
            }
        });
    }
}