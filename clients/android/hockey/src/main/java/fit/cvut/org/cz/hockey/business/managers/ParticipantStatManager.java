package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 2.12.2016.
 */
public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    protected HockeyDBHelper sportDBHelper;

    public ParticipantStatManager(Context context, ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<ParticipantStat, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getHockeyParticipantStatDAO();
    }

    @Override
    public List<ParticipantStat> getByParticipantId(long participantId) {
        try {
            List<ParticipantStat> stats = getDao().queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            return new ArrayList<>(stats);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public int getScoreByParticipantId(long participantId) {
        try {
            List<ParticipantStat> stats = getDao().queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            if (stats.isEmpty())
                return 0;
            return stats.get(0).getScore();
        } catch (SQLException e) {
            return 0;
        }
    }
}
