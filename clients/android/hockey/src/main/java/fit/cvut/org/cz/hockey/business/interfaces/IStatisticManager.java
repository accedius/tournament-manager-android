package fit.cvut.org.cz.hockey.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;

/**
 * Created by kevin on 4.12.2016.
 */
public interface IStatisticManager {
    List<AggregatedStatistics> getByCompetitionId(Context context, long competitionId);
}
