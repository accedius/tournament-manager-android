package fit.cvut.org.cz.bowling.business.managers.interfaces;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

public interface IParticipantManager extends fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager {

    /**
     * get all participants in match with player stats
     * @param matchId id of the match
     * @return found participants
     */
    List<Participant> getByMatchIdWithPlayerStats(long matchId);

    /**
     * get all participants in match with all player stats, frames, rolls etc.
     * @param matchId id of the match
     * @return found participants
     */
    List<Participant> getByMatchIdWithAllContents(long matchId);
}
