package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 9.11.2016.
 */
abstract public class MatchManager extends BaseManager<Match> implements IMatchManager {
    public MatchManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }

    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            return getDao().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
