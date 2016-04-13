package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatsManager implements IStatsManager {
    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(Context context, long competitionId) {

        //TODO remove mock data

        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByCompetition(context, competitionId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }

    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(Context context, long tournamentId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }
}
