package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Participant;

/**
 * Created by kevin on 30.11.2016.
 */
public interface IParticipantManager extends IManager<Participant> {
    /**
     * get all participants in match
     * @param context application context
     * @param matchId id of the match
     * @return found participants
     */
    List<Participant> getByMatchId(Context context, long matchId);

    /**
     * get all participants in match
     * @param context application context
     * @param role role in the match (home/away)
     * @param matchId id of the match
     * @return found participants
     */
    Participant getByRoleAndMatchId(Context context, String role, long matchId);
}
