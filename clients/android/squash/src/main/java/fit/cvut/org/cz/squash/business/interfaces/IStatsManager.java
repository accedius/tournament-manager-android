package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.business.entities.StandingItem;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public interface IStatsManager {

    ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(Context context, long competitionId);
    ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(Context context, long tournamentId);
    ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId);
}
