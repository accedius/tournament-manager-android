package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public interface IPackagePlayerManager {

    void addPlayerToCompetition(Context context, long playerId, long competitionId);
    void addPlayerToTournament(Context context, long playerId, long tournamentId);

    boolean deletePlayerFromCompetition(Context context, long playerId, long competitionId);
    boolean deletePlayerFromTournament(Context context, long playerId, long tournamentId);

    ArrayList<Player> getPlayersByCompetition(Context context, long competitionId);
    ArrayList<Player> getPlayersByTournament(Context context, long tournamentId);
    ArrayList<Player> getPlayersByParticipant(Context context, long participantId);
    ArrayList<Player> getPlayersByTeam(Context context, long teamId);
    ArrayList<Player> getAllPlayers(Context context);

    ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId);
    ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId);

    void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players);
    ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId);

    void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players);
    ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId);
}
