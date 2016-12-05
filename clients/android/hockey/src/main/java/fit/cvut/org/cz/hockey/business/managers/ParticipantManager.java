package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30.11.2016.
 */
public class ParticipantManager extends BaseManager<Participant> implements IParticipantManager {
    public ParticipantManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<Participant, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getParticipantDAO();
    }

    @Override
    public Participant getById(Context context, long id) {
        Participant participant = super.getById(context, id);
        Team team = ManagerFactory.getInstance(context).teamManager.getById(context, participant.getParticipantId());
        participant.setName(team.getName());
        return participant;
    }

    @Override
    public List<Participant> getByMatchId(Context context, long matchId) {
        try {
            List<Participant> participants = getDao(context).queryForEq(DBConstants.cMATCH_ID, matchId);
            for (Participant participant : participants) {
                Team team = ManagerFactory.getInstance(context).teamManager.getById(context, participant.getParticipantId());
                participant.setName(team.getName());
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
