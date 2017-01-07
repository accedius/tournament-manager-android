package fit.cvut.org.cz.tmlibrary.business.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Participant Stat Manager.
 */
abstract public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    @Override
    public List<ParticipantStat> getByParticipantId(long participantId) {
        try {
            return managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).queryForEq(DBConstants.cTOURNAMENT_ID, participantId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
