package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

public interface IPointConfigurationManager extends IManager<PointConfiguration> {
    /**
     * get all teams in tournament
     * @param tournamentId id of the tournament
     * @return found teams
     */
    List<PointConfiguration> getByTournamentId(long tournamentId);
}
