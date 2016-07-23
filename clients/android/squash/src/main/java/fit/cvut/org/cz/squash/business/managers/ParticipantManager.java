package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.interfaces.IParticipantManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public class ParticipantManager implements IParticipantManager {
    @Override
    public long getTeamIdForMatchParticipant(Context context, long matchId, String role) {

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        for (DParticipant p : participants){
            if (p.getRole().equals(role)) return p.getTeamId();
        }

        return -1;
    }

    @Override
    public void updatePlayersForMatch(Context context, long matchId, String role, ArrayList<Player> players) {
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant participant = null;
        for (DParticipant p : participants){
            if (p.getRole().equals(role)) participant = p;
        }
        DMatch m = DAOFactory.getInstance().matchDAO.getById(context, matchId);
        DTournament t = DAOFactory.getInstance().tournamentDAO.getById(context, m.getTournamentId());
        ManagersFactory.getInstance().playerManager.updatePlayersInParticipant(context, participant.getId(), t.getCompetitionId(), t.getId(), players);
    }

    @Override
    public void setParticipationValid(Context context, long matchId) {
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);

        for (DParticipant p : participants){
            DStat stat = DAOFactory.getInstance().statDAO.getByParticipant(context, p.getId(), StatsEnum.MATCH_PARTICIPATION).get(0);
            stat.setStatus(1);
            DAOFactory.getInstance().statDAO.update(context, stat);
        }
    }
}
