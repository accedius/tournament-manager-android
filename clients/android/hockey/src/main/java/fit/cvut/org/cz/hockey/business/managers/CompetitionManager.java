package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.util.List;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionManager extends fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager {
    protected HockeyDBHelper sportDBHelper;

    public CompetitionManager(ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Competition, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getCompetitionDAO();
    }
}
