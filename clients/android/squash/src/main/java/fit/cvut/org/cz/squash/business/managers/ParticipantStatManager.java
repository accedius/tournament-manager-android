package fit.cvut.org.cz.squash.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Created by kevin on 9.12.2016.
 */

public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    @Override
    protected Class<ParticipantStat> getMyClass() {
        return ParticipantStat.class;
    }

    @Override
    public List<ParticipantStat> getByParticipantId(long participantId) {
        try {
            List<ParticipantStat> stats = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            return new ArrayList<>(stats);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteByMatchId(long matchId) {
        try {
            List<Participant> participants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(matchId);
            DeleteBuilder<ParticipantStat, Long> deleteBuilder = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).deleteBuilder();
            for (Participant participant : participants) {
                deleteBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                deleteBuilder.delete();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
