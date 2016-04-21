package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.daos.ParticipantDAO;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {
    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {
        ArrayList<ScoredMatch> matches = new ArrayList<>();

        ArrayList<DMatch> dMatches = DAOFactory.getInstance().matchDAO.getByTournamentId(context, tournamentId);
        for (DMatch dMatch : dMatches){

            ScoredMatch match = new ScoredMatch(dMatch);


        }


        return matches;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {
        return null;
    }

    @Override
    public void beginMatch(Context context, long matchId) {

    }

    @Override
    public void insert(Context context, ScoredMatch match) {

        long matchId = DAOFactory.getInstance().matchDAO.insert(context, ScoredMatch.convertToDMatch(match));
        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, match.getTournamentId());
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        DParticipant home = null;
        DParticipant away = null;
        if (type == CompetitionType.Individuals){
            home = new DParticipant(-1, -1, matchId, "home");
            away = new DParticipant(-1, -1, matchId, "away");
            //DStat hStat =
        } else {
            home = new DParticipant(-1, match.getHomeParticipantId(), matchId, "home");
            away = new DParticipant(-1, match.getAwayParticipantId(), matchId, "away");
        }

        //TODO insert participation

        DAOFactory.getInstance().participantDAO.insert(context, home, false);
        DAOFactory.getInstance().participantDAO.insert(context, away, false);
    }

    @Override
    public void update(Context context, ScoredMatch match) {

    }

    @Override
    public void delete(Context context, long id) {

    }
}
