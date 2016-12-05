package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public interface IScoredMatchManager {
    /**
     * get all matches in tournament
     * @param context application context
     * @param tournamentId id of the tournament, where matches should be found
     * @return found matches
     */
    List<ScoredMatch> getByTournamentId(Context context, long tournamentId);

    /**
     *
     * @param context application context
     * @param Id id of the match
     * @return found match
     */
    ScoredMatch getById(Context context, long Id);

    /**
     * do things necessary to set match from not played to played
     * @param context application context
     * @param matchId id of match to be started
     */
    void beginMatch(Context context, long matchId); //register players to match and other things needed when match is begun

    /**
     * generate next round of matches
     * @param context application context
     * @param tournamentId id of tournament where it should be generated
     */
    void generateRound(Context context, long tournamentId);

    /**
     * reset the match - delete its stats and players and set it to not played
     * @param context application context
     * @param matchId id of the match
     */
    void resetMatch(Context context, long matchId);

    /**
     * insert match
     * @param context application context
     * @param match match to be inserted
     * @return id of inserted match
     */
    long insert(Context context, ScoredMatch match);

    /**
     * update match
     * @param context application context
     * @param match match to be updated
     */
    void update(Context context, ScoredMatch match);

    /**
     * delete match
     * @param context application context
     * @param id id of the match to be deleted
     */
    void delete(Context context, long id);
}
