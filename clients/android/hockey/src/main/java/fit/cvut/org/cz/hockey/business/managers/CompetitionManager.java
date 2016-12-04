package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionManager extends fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager {

    public CompetitionManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<Competition, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getCompetitionDAO();
    }
}
