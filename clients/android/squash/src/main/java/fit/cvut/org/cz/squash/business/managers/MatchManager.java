package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

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
            ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, dMatch.getId());
            DParticipant home = null;
            DParticipant away = null;
            for (DParticipant participant : participants){
                if (participant.getRole().equals("home")) home = participant;
                else away = participant;
            }
            if (home.getTeamId() != -1){
                DTeam t = DAOFactory.getInstance().teamDAO.getById(context, home.getTeamId());
                match.setHomeName(t.getName());
                t = DAOFactory.getInstance().teamDAO.getById(context, away.getTeamId());
                match.setAwayName(t.getName());
            } else {
                String homeName = ManagersFactory.getInstance().playerManager.getPlayersByParticipant(context, home.getId()).get(0).getName();
                String awayName = ManagersFactory.getInstance().playerManager.getPlayersByParticipant(context, away.getId()).get(0).getName();
                match.setHomeName(homeName);
                match.setAwayName(awayName);
            }

            //TODO score


            matches.add(match);
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
            long homePartipId = DAOFactory.getInstance().participantDAO.insert(context, home);
            long awayPartipId = DAOFactory.getInstance().participantDAO.insert(context, away);
            //for individuals we have to insert match participation as well else we could not link match to player
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), match.getHomeParticipantId(), homePartipId, -1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), match.getAwayParticipantId(), awayPartipId, -1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
        } else {
            home = new DParticipant(-1, match.getHomeParticipantId(), matchId, "home");
            away = new DParticipant(-1, match.getAwayParticipantId(), matchId, "away");
            DAOFactory.getInstance().participantDAO.insert(context, home);
            DAOFactory.getInstance().participantDAO.insert(context, away);
        }

    }

    @Override
    public void update(Context context, ScoredMatch match) {

    }

    @Override
    public void delete(Context context, long id) {

    }
}
