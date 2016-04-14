package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager {

    private AgregatedStatistics playerStatsInComp( Context context, long plId, String pName,  long compId )
    {
        //TODO remove mock
        return new AgregatedStatistics( plId, pName, 10, 10, 10, 10, 10, 10, 10, 10 );
    }

    private AgregatedStatistics playerStatsInTourn( Context context, long plId, String pName,  long tourId )
    {
        //TODO remove mock
        return new AgregatedStatistics( plId, pName, 10, 10, 10, 10, 10, 10, 10, 10 );
    }

    public ArrayList<AgregatedStatistics> getByCompetitionID( Context context, long compId )
    {
        ArrayList<Player> compPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByCompetition( context, compId );

        ArrayList<AgregatedStatistics> res = new ArrayList<>();

        for( Player p : compPlayers )
        {
            res.add( playerStatsInComp( context, p.getId(), p.getName(), compId ) );
        }

        return res;
    }

    public ArrayList<AgregatedStatistics> getByTournamentID( Context context, long tourId )
    {
        ArrayList<Player> tourPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament( context, tourId );

        ArrayList<AgregatedStatistics> res = new ArrayList<>();

        for( Player p : tourPlayers )
        {
            res.add( playerStatsInTourn( context, p.getId(), p.getName(), tourId ) );
        }

        return res;
    }
}
