package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public interface IParticipantManager {

    long getTeamIdForMatchParticipant(Context context, long matchId, String role);
    void updatePlayersForMatch(Context context, long matchId, String role, ArrayList<Player> players);
    void setParticipationValid(Context context, long matchId);
}
