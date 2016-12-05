package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 2.12.2016.
 */
public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    public ParticipantStatManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<ParticipantStat, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getHockeyParticipantStatDAO();
    }

    @Override
    public List<ParticipantStat> getByParticipantId(Context context, long participantId) {
        try {
            List<ParticipantStat> stats = getDao(context).queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            return new ArrayList<>(stats);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public int getScoreByParticipantId(Context context, long participantId) {
        try {
            List<ParticipantStat> stats = getDao(context).queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            if (stats.isEmpty())
                return 0;
            return stats.get(0).getScore();
        } catch (SQLException e) {
            return 0;
        }
    }
}
