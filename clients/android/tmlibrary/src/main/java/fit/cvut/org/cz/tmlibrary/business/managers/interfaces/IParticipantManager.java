package fit.cvut.org.cz.tmlibrary.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

/**
 * Created by kevin on 30.11.2016.
 */
public interface IParticipantManager extends IManager<Participant> {
    /**
     * get all participants in match
     * @param matchId id of the match
     * @return found participants
     */
    List<Participant> getByMatchId(long matchId);

    /**
     * get all participants in match
     * @param role role in the match (home/away)
     * @param matchId id of the match
     * @return found participants
     */
    Participant getByRoleAndMatchId(String role, long matchId);
}
