package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tournamentmanager.data.DAOFactory;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager implements IPlayerManager {
    @Override
    public void insert(Context context, Player player) {
        DAOFactory.getInstance().playerDAO.insert(context, Player.convertToDPlayer(player));
    }

    @Override
    public void update(Context context, Player player) {
        DAOFactory.getInstance().playerDAO.update(context, Player.convertToDPlayer(player));
    }

    @Override
    public void delete(Context context, long id) {
        DAOFactory.getInstance().playerDAO.delete(context, id);
    }

    @Override
    public Player getById(Context context, long id) {
        return new Player(DAOFactory.getInstance().playerDAO.getById(context, id));
    }

    @Override
    public ArrayList<Player> getAll(Context context) {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<DPlayer> dplayers = DAOFactory.getInstance().playerDAO.getAll(context);
        for (DPlayer d : dplayers) {
            players.add(new Player(d));
        }
        return players;
    }
}
