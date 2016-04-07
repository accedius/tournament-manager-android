package fit.cvut.org.cz.squash.buisness.managers;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.buisness.entities.AgregatedStats;
import fit.cvut.org.cz.squash.buisness.interfaces.IStatsManager;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatsManager implements IStatsManager {
    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(long competitionId) {

        //TODO remove mock data

        ArrayList<AgregatedStats> stats = new ArrayList<>();

        stats.add(new AgregatedStats(0, "Pete", 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Kate", 9999, 42, 1, 150, 90, 999999, 99999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Rocky", 12, 3, 2, 36, 23, 999999, 99999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Pete", 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Kate", 9999, 42, 1, 150, 90, 999999, 99999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Rocky", 12, 3, 2, 36, 23, 999999, 99999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        stats.add(new AgregatedStats(0, "Pete", 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Kate", 9999, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Rocky", 12, 3, 2, 36, 23, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        stats.add(new AgregatedStats(0, "Pete", 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Kate", 9999, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));
        stats.add(new AgregatedStats(0, "Rocky", 12, 3, 2, 36, 23, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));


        return stats;
    }

    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(long tournamentId) {
        return null;
    }
}
