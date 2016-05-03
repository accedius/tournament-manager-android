package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public interface IPointConfigManager {

    /**
     * Updates the point configuration of a tournament
     * @param context
     * @param pointConfiguration point configuration to be updated to
     * @param tournamentId id of tournament to be updated
     */
    void update( Context context, PointConfiguration pointConfiguration, Long tournamentId );

    /**
     *
     * @param context
     * @param tournamentId id of tournament
     * @return the requested configuration of the given tournament
     */
    PointConfiguration getByTournamentId( Context context, Long tournamentId );

}
