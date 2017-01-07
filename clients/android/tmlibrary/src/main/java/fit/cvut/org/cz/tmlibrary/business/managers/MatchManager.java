package fit.cvut.org.cz.tmlibrary.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Match Manager.
 */
abstract public class MatchManager extends BaseManager<Match> implements IMatchManager {
    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            return managerFactory.getDaoFactory().getMyDao(Match.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
