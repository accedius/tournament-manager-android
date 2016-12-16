package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 6.11.2016.
 */
public interface ICompetitionManager extends IManager<Competition> {
    /**
     * add player to competition
     * @param competition competition where player should be added
     * @param player player to be added
     */
    void addPlayer(Competition competition, Player player);

    /**
     * get all players in competition
     * @param competitionId id of competition
     * @return found players
     */
    List<Player> getCompetitionPlayers(long competitionId);

    /**
     * get all players not in competition
     * @param competitionId id of competition
     * @return found players
     */
    List<Player> getCompetitionPlayersComplement(long competitionId);

    /**
     * remove player from competition
     * @param playerId id of player to be removed
     * @param competitionId id of competition
     * @return true if player was removed, false otherwise
     */
    boolean removePlayerFromCompetition(long playerId, long competitionId);

    /**
     * get all tournaments in competition
     * @param competitionId id of the competition
     * @return found tournaments
     */
    List<Tournament> getByCompetitionId(long competitionId);
}
