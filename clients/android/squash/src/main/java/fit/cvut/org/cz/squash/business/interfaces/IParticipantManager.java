package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public interface IParticipantManager {
    /**
     *
     * @param context
     * @param matchId of match
     * @param role of participant either home or away
     * @return id of team participant is bound to
     */
    long getTeamIdForMatchParticipant(Context context, long matchId, String role);

    /**
     *
     * @param context
     * @param matchId
     * @param role either home or away
     * @param players list of players to be bound to participant with given role in given match
     */
    void updatePlayersForMatch(Context context, long matchId, String role, ArrayList<Player> players);

    /**
     * Sets all participations for given match valid and they now will be counted in stats
     * @param context
     * @param matchId
     */
    void setParticipationValid(Context context, long matchId);
}
