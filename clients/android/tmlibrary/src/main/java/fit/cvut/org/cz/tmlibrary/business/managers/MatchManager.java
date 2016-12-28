package fit.cvut.org.cz.tmlibrary.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;

/**
 * Created by kevin on 9.11.2016.
 */
abstract public class MatchManager extends TManager<Match> implements IMatchManager {
    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            return managerFactory.getDaoFactory().getMyDao(Match.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
