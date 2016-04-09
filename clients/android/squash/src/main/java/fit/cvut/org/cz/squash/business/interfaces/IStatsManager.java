package fit.cvut.org.cz.squash.business.interfaces;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.entities.AgregatedStats;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public interface IStatsManager {

    ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(long competitionId);
    ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(long tournamentId);
}
