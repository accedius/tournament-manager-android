package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IManager;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public interface IPointConfigManager extends IManager<PointConfiguration> {
    /**
     *
     * @param context
     * @param tournamentId id of tournament
     * @return the requested configuration of the given tournament
     */
    PointConfiguration getByTournamentId(Context context, Long tournamentId);

}
