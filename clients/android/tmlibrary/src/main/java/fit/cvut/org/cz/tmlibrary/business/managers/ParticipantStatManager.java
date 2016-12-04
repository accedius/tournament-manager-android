package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30.11.2016.
 */
abstract public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    public ParticipantStatManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    public List<ParticipantStat> getByParticipantId(Context context, long participantId) {
        try {
            return getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, participantId)
                    .query();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
