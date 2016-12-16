package fit.cvut.org.cz.squash.tlib;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.tlib.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.tlib.TManager;

/**
 * Created by kevin on 14.12.2016.
 */

public class TournamentManager extends TManager<Tournament> implements ITournamentManager {

    @Override
    protected Class<Tournament> getMyClass() {
        return Tournament.class;
    }

    @Override
    protected IDAOFactory getDaoFactory() {
        return SquashManagerFactory.getInstance().getDaoFactory();
    }
    @Override
    public List<Tournament> getByCompetitionId(long competitionId) {
        try {
            return SquashManagerFactory.getInstance().getDaoFactory().getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Player> getTournamentPlayers(long tournamentId) {
        return null;
    }

    @Override
    public List<Player> getTournamentPlayersComplement(long tournamentId) {
        return null;
    }

    @Override
    public boolean removePlayerFromTournament(long playerId, long tournamentId) {
        return false;
    }

    @Override
    public void addPlayer(long playerId, long tournamentId) {

    }

    @Override
    public List<Tournament> getByPlayer(long playerId) {
        return null;
    }
}
