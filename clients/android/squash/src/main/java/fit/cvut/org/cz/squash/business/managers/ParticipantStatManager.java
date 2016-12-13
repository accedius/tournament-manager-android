package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.ParticipantStat;
import fit.cvut.org.cz.squash.business.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 9.12.2016.
 */

public class ParticipantStatManager extends BaseManager<ParticipantStat> implements IParticipantStatManager {
    protected SquashDBHelper sportDBHelper;

    public ParticipantStatManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<ParticipantStat, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getSquashParticipantStatDAO();
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
    public void deleteByMatchId(long matchId) {
        try {
            List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(matchId);
            DeleteBuilder<ParticipantStat, Long> deleteBuilder = getDao().deleteBuilder();
            for (Participant participant : participants) {
                deleteBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                deleteBuilder.delete();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

