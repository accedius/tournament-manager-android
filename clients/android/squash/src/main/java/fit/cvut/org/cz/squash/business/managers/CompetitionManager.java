package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by Vaclav on 30. 3. 2016.
 */
public class CompetitionManager extends fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager {
    protected SquashDBHelper sportDBHelper;

    public CompetitionManager(ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Competition, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getCompetitionDAO();
    }
}
