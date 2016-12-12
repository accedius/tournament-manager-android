package fit.cvut.org.cz.hockey.business.interfaces;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;

/**
 * Created by kevin on 4.12.2016.
 */
public interface IStatisticManager {
    List<AggregatedStatistics> getByCompetitionId(long competitionId);
}
