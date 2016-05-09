package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public interface IStatsManager {

    ArrayList<SAggregatedStats> getAggregatedStatsByCompetitionId(Context context, long competitionId);
    ArrayList<SAggregatedStats> getAggregatedStatsByTournamentId(Context context, long tournamentId);
    ArrayList<SAggregatedStats> getAggregatedStatsByPlayerId(Context context, long playerID);
    ArrayList<SAggregatedStats> getAllAggregatedStats(Context context);
    ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId);
    ArrayList<SetRowItem> getSetsForMatch(Context context, long matchId);

    void updateStatsForMatch(Context context, long matchId, ArrayList<SetRowItem> sets);
    ArrayList<Player> getPlayersForMatch(Context context, long matchId, String role);

}
