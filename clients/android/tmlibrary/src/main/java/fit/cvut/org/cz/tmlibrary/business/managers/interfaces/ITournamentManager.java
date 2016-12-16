package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by Vaclav on 29. 3. 2016.
 */
public interface ITournamentManager extends IManager<Tournament> {
    /**
     * add player to tournament
     * @param playerId id of player to be added
     * @param tournamentId id of tournament
     */
    void addPlayer(long playerId, long tournamentId);

    /**
     * get all players in tournament
     * @param tournamentId id of tournament
     * @return found players
     */
    List<Player> getTournamentPlayers(long tournamentId);

    /**
     * get all players not in tournament
     * @param tournamentId id of tournament
     * @return found players
     */
    List<Player> getTournamentPlayersComplement(long tournamentId);

    /**
     * remove player from tournament
     * @param playerId id of player to be removed
     * @param tournamentId id of tournament
     * @return true if player was removed, false otherwise
     */
    boolean removePlayerFromTournament(long playerId, long tournamentId);

    /**
     * get all tournaments for player
     * @param playerId id of player
     * @return found tournaments
     */
    List<Tournament> getByPlayer(long playerId);

    /**
     * get all tournaments in competition
     * @param competitionId id of the competition
     * @return found tournaments
     */
    List<Tournament> getByCompetitionId(long competitionId);
}
