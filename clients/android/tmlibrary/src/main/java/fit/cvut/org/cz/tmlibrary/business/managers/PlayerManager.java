package fit.cvut.org.cz.tmlibrary.business.managers;

/**
 * Created by kevin on 6.11.2016.
 */

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class PlayerManager implements IManager<Player> {
    protected abstract Dao<Player, Long> getDao(Context context);

    @Override
    public void insert(Context context, Player player) {
        try {
            getDao(context).create(player);
        } catch (SQLException e) {}
    }

    @Override
    public void update(Context context, Player player) {
        try {
            getDao(context).update(player);
        } catch (SQLException e) {}
    }

    @Override
    public boolean delete(Context context, long id) {
        try {
            getDao(context).delete(getDao(context).queryForId(id));
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Player getById(Context context, long id) {
        try {
            return getDao(context).queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Player> getAll(Context context) {
        ArrayList<Player> ret = new ArrayList<>();
        try {
            for (Player p : getDao(context).queryForAll()) {
                ret.add(p);
            }
        } catch (SQLException e) {
            return new ArrayList<Player>();
        }
        return ret;
    }
}
