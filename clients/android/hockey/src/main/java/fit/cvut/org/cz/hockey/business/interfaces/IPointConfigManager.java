package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public interface IPointConfigManager {

    void update( Context context, PointConfiguration pointConfiguration, Long tournamentId );

    PointConfiguration getByTournamentId( Context context, Long tournamentId );

}
