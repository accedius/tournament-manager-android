package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30.11.2016.
 */
abstract public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    public ParticipantStatManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }

    @Override
    public List<ParticipantStat> getByParticipantId(long participantId) {
        try {
            return getDao().queryForEq(DBConstants.cTOURNAMENT_ID, participantId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
