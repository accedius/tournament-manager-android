package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager extends fit.cvut.org.cz.tmlibrary.business.managers.PlayerManager {
    public PlayerManager(Context context) {
        super(context);
    }

    protected Dao<Player, Long> getDao() {
        return DatabaseFactory.getDBHelper(context).getPlayerDao();
    }
}
