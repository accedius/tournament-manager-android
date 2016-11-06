package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPlayerManager;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class PlayerManager extends fit.cvut.org.cz.tmlibrary.business.managers.PlayerManager {
    protected Dao<Player, Long> getDao(Context context) {
        return DatabaseFactory.getDBHelper(context).getPlayerDao();
    }
}
