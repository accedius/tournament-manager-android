package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.RoundRobinScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {
    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {
        ArrayList<ScoredMatch> matches = new ArrayList<>();

        ArrayList<DMatch> dMatches = DAOFactory.getInstance().matchDAO.getByTournamentId(context, tournamentId);
        for (DMatch dMatch : dMatches) {
            ScoredMatch match = new ScoredMatch(dMatch);
            ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, dMatch.getId());
            DParticipant home = null;
            DParticipant away = null;
            for (DParticipant participant : participants) {
                if (participant.getRole().equals("home")) home = participant;
                else away = participant;
            }
            if (home.getTeamId() != -1) {
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

            if (match.isPlayed()) {
                int homeScore = 0, awayScore = 0;
                ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getByParticipant(context, home.getId(), StatsEnum.SET);
                for (DStat stat : stats) {
                    if (stat.getStatus() > 0) homeScore++;
                    else awayScore++;
                }
                match.setHomeScore(homeScore);
                match.setAwayScore(awayScore);
            }

            matches.add(match);
        }

        Collections.sort(matches, new Comparator<ScoredMatch>() {
            @Override
            public int compare(ScoredMatch lhs, ScoredMatch rhs) {
                if (rhs.getRound() - lhs.getRound() == 0)
                    return lhs.getPeriod() - rhs.getPeriod();
                return lhs.getRound() - rhs.getRound();
            }
        });
        return matches;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {
        ScoredMatch match = new ScoredMatch(DAOFactory.getInstance().matchDAO.getById(context, Id));

        DTournament t = DAOFactory.getInstance().tournamentDAO.getById(context, match.getTournamentId());
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, Id);
        DParticipant home = null, away = null;
        for (DParticipant p : participants) {
            if (p.getRole().equals("home")) home = p;
            else away = p;
        }

        if (type.equals(CompetitionTypes.teams())) {
            match.setHomeParticipantId(home.getTeamId());
            match.setAwayParticipantId(away.getTeamId());
            match.setHomeName(ManagersFactory.getInstance().teamsManager.getById(context, home.getTeamId()).getName());
            match.setAwayName(ManagersFactory.getInstance().teamsManager.getById(context, away.getTeamId()).getName());
        } else {
            match.setHomeParticipantId(DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, home.getId()).get(0));
            match.setAwayParticipantId(DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, away.getId()).get(0));
            match.setHomeName(ManagersFactory.getInstance().playerManager.getPlayersByParticipant(context, home.getId()).get(0).getName());
            match.setAwayName(ManagersFactory.getInstance().playerManager.getPlayersByParticipant(context, away.getId()).get(0).getName());
        }

        return match;
    }

    @Override
    public void beginMatch(Context context, long matchId) {
    }

    @Override
    public void generateRound(Context context, long tournamentId) {
        DTournament tournament = DAOFactory.getInstance().tournamentDAO.getById(context, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, tournament.getCompetitionId()).getType();

        ArrayList<DMatch> matches = DAOFactory.getInstance().matchDAO.getByTournamentId(context, tournamentId);
        Collections.sort(matches, new Comparator<DMatch>() {
            @Override
            public int compare(DMatch lhs, DMatch rhs) {
                if (rhs.getRound() - lhs.getRound() == 0)
                    return lhs.getPeriod() - rhs.getPeriod();
                return lhs.getRound() - rhs.getRound();
            }
        });

        ArrayList<NewMatchSpinnerParticipant> participants = new ArrayList<>();

        if (type.equals(CompetitionTypes.individuals())) {
            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
            for (Player p : players) participants.add(new NewMatchSpinnerParticipant(p.getId(), p.getName()));
        } else {
            ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
            for (Team t : teams) participants.add(new NewMatchSpinnerParticipant(t.getId(), t.getName()));
        }

        IScoredMatchGenerator generator = new RoundRobinScoredMatchGenerator();
        int round = -1;
        if (matches.size() != 0) round = matches.get(matches.size() -1).getRound();
        round++;
        ArrayList<ScoredMatch> newMatches = generator.generateRound(participants, round);

        for (ScoredMatch m : newMatches) {
            m.setTournamentId(tournamentId);
            insert(context, m);
        }
    }

    @Override
    public void resetMatch(Context context, long matchId) {
        ScoredMatch match = new ScoredMatch(DAOFactory.getInstance().matchDAO.getById(context, matchId));
        match.setPlayed(false);
        update(context, match);

        DTournament t = DAOFactory.getInstance().tournamentDAO.getById(context, match.getTournamentId());
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        for (DParticipant p : participants) {
            DAOFactory.getInstance().statDAO.delete(context, p.getId(), StatsEnum.MATCH);
            DAOFactory.getInstance().statDAO.delete(context, p.getId(), StatsEnum.SET);
            if (type.equals(CompetitionTypes.teams()))
                DAOFactory.getInstance().statDAO.delete(context, p.getId(), StatsEnum.MATCH_PARTICIPATION);
        }
    }

    @Override
    public void insert(Context context, ScoredMatch match) {
        long matchId = DAOFactory.getInstance().matchDAO.insert(context, ScoredMatch.convertToDMatch(match));
        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, match.getTournamentId());
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        DParticipant home = null;
        DParticipant away = null;
        if (type.equals(CompetitionTypes.individuals())) {
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
        DAOFactory.getInstance().matchDAO.update(context, ScoredMatch.convertToDMatch(match));
    }

    @Override
    public void delete(Context context, long id) {
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, id);
        for (DParticipant participant : participants) {
            DAOFactory.getInstance().statDAO.deleteByParticipant(context, participant.getId());
            DAOFactory.getInstance().participantDAO.delete(context, participant.getId());
        }

        DAOFactory.getInstance().matchDAO.delete(context, id);
    }
}
