package fit.cvut.org.cz.squash.tlib;

import java.util.List;

import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.tlib.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.tlib.TManager;

/**
 * Created by kevin on 14.12.2016.
 */

public class CompetitionManager extends TManager<Competition> implements ICompetitionManager {

    @Override
    protected Class<Competition> getMyClass() {
        return Competition.class;
    }
    @Override
    public void addPlayer(Competition competition, Player player) {

    }

    @Override
    public List<Player> getCompetitionPlayers(long competitionId) {
        return null;
    }

    @Override
    public List<Player> getCompetitionPlayersComplement(long competitionId) {
        return null;
    }

    @Override
    public boolean removePlayerFromCompetition(long playerId, long competitionId) {
        return false;
    }

    @Override
    protected IDAOFactory getDaoFactory() {
        return SquashManagerFactory.getInstance().getDaoFactory();
    }
}
