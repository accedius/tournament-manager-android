package fit.cvut.org.cz.hockey.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public interface IPointConfigDAO {
    void update( Context context, DPointConfiguration dPointConfiguration, Long tournamentId);

    public long insertDefault( Context context, long tournamentId);

    DPointConfiguration getByTournamentId( Context context, Long tournamentId );
}
