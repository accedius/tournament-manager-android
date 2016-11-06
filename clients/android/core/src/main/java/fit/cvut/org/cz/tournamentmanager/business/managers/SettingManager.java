package fit.cvut.org.cz.tournamentmanager.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Setting;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tournamentmanager.data.DatabaseFactory;

/**
 * Created by kevin on 23.10.2016.
 */
public class SettingManager extends fit.cvut.org.cz.tmlibrary.business.managers.SettingManager {
    protected Dao<Setting, Long> getDao(Context context) {
        return DatabaseFactory.getDBHelper(context).getSettingDao();
    }
}
