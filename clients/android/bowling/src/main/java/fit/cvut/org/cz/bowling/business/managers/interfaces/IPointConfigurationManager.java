package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;

public interface IPointConfigurationManager extends IManager<PointConfiguration> {
    /**
     * get all point configurations in tournament
     * @param tournamentId id of the tournament
     * @return found point configurations
     */
    List<PointConfiguration> getByTournamentId(long tournamentId);

    /**
     * get point configuration of declared tournament for stated number of match's competing sides
     * @param tournamentId id of the tournament
     * @param sidesNumber number of match's competing sides
     * @return found point configuration
     */
    public PointConfiguration getBySidesNumber (long tournamentId, long sidesNumber);
}
