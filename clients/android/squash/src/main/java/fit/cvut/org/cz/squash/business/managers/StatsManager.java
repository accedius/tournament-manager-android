package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

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

    @Override
    public ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId) {

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();

        ArrayList<StandingItem> standings = new ArrayList<>();

        if (type == CompetitionType.Individuals){

            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);

            for (Player p : players) standings.add(new StandingItem(p.getName(), 3, 1, 0, 12, 6, 9));
        } else {
            //team competition
            ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
            for (Team team : teams) standings.add(new StandingItem(team.getName(), 2, 1, 2, 6, 5, 7));
        }
        Collections.sort(standings, new Comparator<StandingItem>() {
            @Override
            public int compare(StandingItem lhs, StandingItem rhs) {
                return lhs.getPoints() - rhs.getPoints();
            }
        });
        return standings;
    }
}
