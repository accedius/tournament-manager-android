package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.ParticipantStat;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public class ParticipantManager extends BaseManager<Participant> implements fit.cvut.org.cz.tmlibrary.business.interfaces.IParticipantManager {
    protected SquashDBHelper sportDBHelper;

    public ParticipantManager(ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Participant, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getParticipantDAO();
    }

    @Override
    public List<Participant> getByMatchId(Context context, long matchId) {
        try {
            List<Participant> participants = getDao(context).queryForEq(DBConstants.cMATCH_ID, matchId);
            for (Participant participant : participants) {
                List<ParticipantStat> participantStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(context, participant.getId());
                participant.setParticipantStats(participantStats);
            }
            return new ArrayList<>(participants);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Participant getByRoleAndMatchId(Context context, String role, long matchId) {
        if (role == null)
            return null;
        List<Participant> participants = getByMatchId(context, matchId);
        for (Participant participant : participants) {
            if (role.equals(participant.getRole())) {
                return participant;
            }
        }
        return null;
    }
}
