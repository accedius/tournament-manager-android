package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.business.entities.PointConfig;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatsManager implements IStatsManager {
    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(Context context, long competitionId) {

        //TODO remove mock data

        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByCompetition(context, competitionId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }

    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(Context context, long tournamentId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }

    @Override
    public ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId) {

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        Map<Long, StandingItem> mappedStandings = new HashMap<>();

        ArrayList<StandingItem> standings = new ArrayList<>();
        ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getByTournament(context, t.getId(), StatsEnum.MATCH);
        PointConfig cfg = ManagersFactory.getInstance().pointConfigManager.getById(context, tournamentId);

        if (type == CompetitionType.Individuals){

            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
            for (Player p : players) mappedStandings.put(p.getId(), new StandingItem(p.getName()));
            for (DStat stat : stats){
                DParticipant p = DAOFactory.getInstance().participantDAO.getById(context, stat.getParticipantId());
                long playerId = DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, p.getId()).get(0);
                ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByParticipant(context, p.getId(), StatsEnum.SET);
                for (DStat set : sets){
                    if (set.getStatus() == 1) mappedStandings.get(playerId).setSetsWon(mappedStandings.get(playerId).getSetsWon() + 1);
                    else mappedStandings.get(playerId).setSetsLost(mappedStandings.get(playerId).getSetsLost() + 1);
                }
                switch (stat.getStatus()){
                    case 0:
                        mappedStandings.get(playerId).setDraws(mappedStandings.get(playerId).getDraws() + 1);
                        mappedStandings.get(playerId).setPoints(mappedStandings.get(playerId).getPoints() + cfg.getDraw());
                        break;
                    case -1:
                        mappedStandings.get(playerId).setLoses(mappedStandings.get(playerId).getLoses() + 1);
                        mappedStandings.get(playerId).setPoints(mappedStandings.get(playerId).getPoints() + cfg.getLoss());
                        break;
                    case 1:
                        mappedStandings.get(playerId).setWins(mappedStandings.get(playerId).getWins() + 1);
                        mappedStandings.get(playerId).setPoints(mappedStandings.get(playerId).getPoints() + cfg.getWin());
                        break;
                }

            }
        } else {
            //team competition
            ArrayList<DTeam> teams = DAOFactory.getInstance().teamDAO.getByTournamentId(context, tournamentId);
            for (DTeam team : teams) mappedStandings.put(team.getId(), new StandingItem(team.getName()));

            for (DStat stat : stats){
                DParticipant p = DAOFactory.getInstance().participantDAO.getById(context, stat.getParticipantId());
                ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByParticipant(context, p.getId(), StatsEnum.SET);
                for (DStat set : sets){
                    if (set.getStatus() == 1) mappedStandings.get(p.getTeamId()).setSetsWon(mappedStandings.get(p.getTeamId()).getSetsWon() + 1);
                    else mappedStandings.get(p.getTeamId()).setSetsLost(mappedStandings.get(p.getTeamId()).getSetsLost() + 1);
                }
                switch (stat.getStatus()){
                    case 0:
                        mappedStandings.get(p.getTeamId()).setDraws(mappedStandings.get(p.getTeamId()).getDraws() + 1);
                        mappedStandings.get(p.getTeamId()).setPoints(mappedStandings.get(p.getTeamId()).getPoints() + cfg.getDraw());
                        break;
                    case -1:
                        mappedStandings.get(p.getTeamId()).setLoses(mappedStandings.get(p.getTeamId()).getLoses() + 1);
                        mappedStandings.get(p.getTeamId()).setPoints(mappedStandings.get(p.getTeamId()).getPoints() + cfg.getLoss());
                        break;
                    case 1:
                        mappedStandings.get(p.getTeamId()).setWins(mappedStandings.get(p.getTeamId()).getWins() + 1);
                        mappedStandings.get(p.getTeamId()).setPoints(mappedStandings.get(p.getTeamId()).getPoints() + cfg.getWin());
                        break;
                }

            }
        }
        for (Long key : mappedStandings.keySet()) standings.add(mappedStandings.get(key));
        Collections.sort(standings, new Comparator<StandingItem>() {
            @Override
            public int compare(StandingItem lhs, StandingItem rhs) {
                if (rhs.getPoints() - lhs.getPoints() == 0 )
                    return (rhs.getSetsWon() - rhs.getSetsLost() - lhs.getSetsWon() + lhs.getSetsLost());
                return rhs.getPoints() - lhs.getPoints();
            }
        });
        return standings;
    }

    @Override
    public ArrayList<SetRowItem> getSetsForMatch(Context context, long matchId) {
        ArrayList<SetRowItem> sets = new ArrayList<>();

        ArrayList<DParticipant> dparticipants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant home = null, away = null;

        for (DParticipant participant : dparticipants){
            if (participant.getRole().equals("home")) home = participant;
            else away = participant;
        }

        ArrayList<DStat> homeSets = DAOFactory.getInstance().statDAO.getByParticipant(context, home.getId(), StatsEnum.SET);
        ArrayList<DStat> awaySets = DAOFactory.getInstance().statDAO.getByParticipant(context, away.getId(), StatsEnum.SET);

        for (int i = 0; i< homeSets.size(); i++){
            SetRowItem item = new SetRowItem();
            item.setHomeScore(homeSets.get(i).getValue());
            item.setAwayScore(awaySets.get(i).getValue());
            item.setWinner(homeSets.get(i).getStatus());

            sets.add(item);
        }

        return sets;
    }

    @Override
    public void updateStatsForMatch(Context context, long matchId, ArrayList<SetRowItem> sets) {

        DMatch m = DAOFactory.getInstance().matchDAO.getById(context, matchId);
        m.setPlayed(true);
        DAOFactory.getInstance().matchDAO.update(context, m);

        ArrayList<DParticipant> dparticipants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant home = null, away = null;

        for (DParticipant participant : dparticipants){
            DAOFactory.getInstance().statDAO.delete(context, participant.getId(), StatsEnum.SET);
            DAOFactory.getInstance().statDAO.delete(context, participant.getId(), StatsEnum.MATCH);
            if (participant.getRole().equals("home")) home = participant;
            else away = participant;
        }

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, m.getTournamentId());

        int setswon = 0;

        for (int i = 0; i< sets.size(); i++) {
            SetRowItem item = sets.get(i);
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, home.getId(),
                    item.getWinner(), i + 1, item.getHomeScore(), StatsEnum.SET));
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, away.getId(),
                    item.getWinner() * -1, i + 1, item.getAwayScore(), StatsEnum.SET));
            setswon += item.getWinner();
        }
        int result = 0;
        if (setswon > 0) result = 1;
        if (setswon < 0) result = -1;
        DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, home.getId(),
                result, -1, -1, StatsEnum.MATCH));
        DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, away.getId(),
                result * -1, -1, -1, StatsEnum.MATCH));
    }

    @Override
    public ArrayList<Player> getPlayersForMatch(Context context, long matchId, String role) {
        ArrayList<Player> players = new ArrayList<>();

        DMatch m = DAOFactory.getInstance().matchDAO.getById(context, matchId);
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);

        DParticipant participant = null;
        for (DParticipant p : participants) if (p.getRole().equals(role)) participant = p;

        if (!m.isPlayed()){
            return ManagersFactory.getInstance().teamsManager.getById(context, participant.getTeamId()).getPlayers();
        } else {
            ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getByParticipant(context, participant.getId(), StatsEnum.MATCH_PARTICIPATION);
            Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
            for (DStat stat : stats){
                players.add(new Player(allPlayers.get(stat.getPlayerId())));
            }
        }

        return players;
    }
}
