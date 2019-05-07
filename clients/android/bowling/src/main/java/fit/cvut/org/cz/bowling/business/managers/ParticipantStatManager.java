package fit.cvut.org.cz.bowling.business.managers;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    @Override
    protected Class<ParticipantStat> getMyClass() {
        return ParticipantStat.class;
    }

    @Override
    public List<ParticipantStat> getByParticipantId(long participantId) {
        List<ParticipantStat> stats = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).getListItemById(DBConstants.cPARTICIPANT_ID, participantId);
        return new ArrayList<>(stats);
    }

    @Override
    public int getScoreByParticipantId(long participantId) {
        List<ParticipantStat> stats = managerFactory.getDaoFactory().getMyDao(ParticipantStat.class).getListItemById(DBConstants.cPARTICIPANT_ID, participantId);
        if (stats.isEmpty())
            return 0;
        return stats.get(0).getScore();
    }

}
