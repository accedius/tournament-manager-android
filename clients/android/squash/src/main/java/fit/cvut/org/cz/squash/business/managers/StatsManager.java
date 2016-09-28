package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.PointConfig;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatsManager implements IStatsManager {
    private void orderPlayers(ArrayList<SAggregatedStats> stats) {
        Collections.sort(stats, new Comparator<SAggregatedStats>() {
            @Override
            public int compare(SAggregatedStats ls, SAggregatedStats rs) {
                if (rs.points != ls.points)
                    return rs.points - ls.points;
                if (rs.setsWon != ls.setsWon) {
                    return rs.setsWon- ls.setsWon;
                }
                return ls.games_played-rs.games_played;
            }
        });
    }

    private ArrayList<SAggregatedStats> getStats(Context context, ArrayList<Player> players, ArrayList<DStat> matchResults, ArrayList<DStat> sets){
        ArrayList<SAggregatedStats> stats = new ArrayList<>();
        Map<Long, SAggregatedStats> mappedStats = new HashMap<>();
        Map<Long, ArrayList<Long>> mappedParticipants = new HashMap<>();
        Map<Long, PointConfig> tournamentPointConfigs = new HashMap<>();

        for (DStat result : matchResults) {
            if (!tournamentPointConfigs.containsKey(result.getTournamentId())) {
                PointConfig cfg = ManagersFactory.getInstance().pointConfigManager.getById(
                                    context, result.getTournamentId());
                tournamentPointConfigs.put(result.getTournamentId(), cfg);
            }
        }

        for (Player p : players) mappedStats.put(p.getId(), new SAggregatedStats(p.getName(), p.getId()));
        for (DStat result : matchResults) {
            if (!mappedParticipants.containsKey(result.getParticipantId())) {
                mappedParticipants.put(result.getParticipantId(), DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, result.getParticipantId()));
            }
            for (Long playerId : mappedParticipants.get(result.getParticipantId())) {
                if (mappedStats.get(playerId) == null)
                    continue;
                mappedStats.get(playerId).games_played++;
                switch (result.getStatus()) {
                    case -1:
                        mappedStats.get(playerId).lost++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(result.getTournamentId()).getLoss();
                        break;
                    case 0:
                        mappedStats.get(playerId).draws++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(result.getTournamentId()).getDraw();
                        break;
                    case 1:
                        mappedStats.get(playerId).won++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(result.getTournamentId()).getWin();
                        break;
                }
            }
        }
        for (DStat set : sets) {
            for (Long playerId : mappedParticipants.get(set.getParticipantId())) {
                if (mappedStats.get(playerId) == null)
                    continue;

                if (set.getStatus() == 1) mappedStats.get(playerId).setsWon++;
                else mappedStats.get(playerId).setsLost++;
                mappedStats.get(playerId).ballsWon += set.getValue();
                mappedStats.get(playerId).ballsLost += set.getLostValue();
            }
        }

        for (Long key : mappedStats.keySet()) {
            double played = mappedStats.get(key).won + mappedStats.get(key).lost + mappedStats.get(key).draws;
            if (played > 0) {
                mappedStats.get(key).matchWinRate = mappedStats.get(key).won / played * 100;
                mappedStats.get(key).setsWinRate = 0;
                if (mappedStats.get(key).setsWon + mappedStats.get(key).setsLost > 0)
                    mappedStats.get(key).setsWinRate = ((double) mappedStats.get(key).setsWon) / (mappedStats.get(key).setsWon + mappedStats.get(key).setsLost) * 100;
                mappedStats.get(key).ballsWonAvg = mappedStats.get(key).ballsWon / played;
                mappedStats.get(key).ballsLostAvg = mappedStats.get(key).ballsLost / played;
                mappedStats.get(key).setsWonAvg = mappedStats.get(key).setsWon / played;
                mappedStats.get(key).setsLostAvg = mappedStats.get(key).setsLost / played;
            }
            stats.add(mappedStats.get(key));
        }

        return stats;
    }

    @Override
    public ArrayList<SAggregatedStats> getAggregatedStatsByCompetitionId(Context context, long competitionId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByCompetition(context, competitionId);
        ArrayList<DStat> results = DAOFactory.getInstance().statDAO.getByCompetition(context, competitionId, StatsEnum.MATCH);
        ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByCompetition(context, competitionId, StatsEnum.SET);

        ArrayList<SAggregatedStats> res = getStats(context, players, results, sets);
        orderPlayers(res);
        return res;
    }

    @Override
    public ArrayList<SAggregatedStats> getAggregatedStatsByTournamentId(Context context, long tournamentId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<DStat> results = DAOFactory.getInstance().statDAO.getByTournament(context, tournamentId, StatsEnum.MATCH);
        ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByTournament(context, tournamentId, StatsEnum.SET);

        ArrayList<SAggregatedStats> res = getStats(context, players, results, sets);
        orderPlayers(res);
        return res;
    }

    @Override
    public ArrayList<SAggregatedStats> getAggregatedStatsByPlayerId(Context context, long playerID) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getAllPlayers(context);
        ArrayList<Player> filtered_players = new ArrayList<>();
        for (Player p : players) {
            if (playerID == p.getId())
                filtered_players.add(p);
        }
        ArrayList<DStat> results = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.MATCH);
        ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.SET);

        return getStats(context, filtered_players, results, sets);
    }

    @Override
    public ArrayList<SAggregatedStats> getAllAggregatedStats(Context context) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getAllPlayers(context);
        ArrayList<DStat> results = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.MATCH);
        ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.SET);

        return getStats(context, players, results, sets);
    }

    @Override
    public ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId) {
        DTournament t = DAOFactory.getInstance().tournamentDAO.getById(context, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();
        Map<Long, StandingItem> mappedStandings = new HashMap<>();

        ArrayList<StandingItem> standings = new ArrayList<>();
        ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getByTournament(context, t.getId(), StatsEnum.MATCH);
        PointConfig cfg = ManagersFactory.getInstance().pointConfigManager.getById(context, tournamentId);

        if (type.equals(CompetitionTypes.individuals())) {
            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
            for (Player p : players) mappedStandings.put(p.getId(), new StandingItem(p.getName()));
            for (DStat stat : stats) {
                long playerId = DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, stat.getParticipantId()).get(0);
                ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByParticipant(context, stat.getParticipantId(), StatsEnum.SET);
                for (DStat set : sets) {
                    if (set.getStatus() == 1) mappedStandings.get(playerId).setsWon++;
                    else mappedStandings.get(playerId).setsLost++;
                }
                switch (stat.getStatus()){
                    case 0:
                        mappedStandings.get(playerId).draws++;
                        mappedStandings.get(playerId).points += cfg.getDraw();
                        break;
                    case -1:
                        mappedStandings.get(playerId).losses++;
                        mappedStandings.get(playerId).points += cfg.getLoss();
                        break;
                    case 1:
                        mappedStandings.get(playerId).wins++;
                        mappedStandings.get(playerId).points+=cfg.getWin();
                        break;
                }
            }
        } else {
            //team competition
            ArrayList<DTeam> teams = DAOFactory.getInstance().teamDAO.getByTournamentId(context, tournamentId);
            for (DTeam team : teams) mappedStandings.put(team.getId(), new StandingItem(team.getName()));

            for (DStat stat : stats) {
                DParticipant p = DAOFactory.getInstance().participantDAO.getById(context, stat.getParticipantId());
                ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getByParticipant(context, p.getId(), StatsEnum.SET);
                for (DStat set : sets) {
                    if (set.getStatus() == 1) mappedStandings.get(p.getTeamId()).setsWon++;
                    else mappedStandings.get(p.getTeamId()).setsLost++;
                }
                switch (stat.getStatus()){
                    case 0:
                        mappedStandings.get(p.getTeamId()).draws++;
                        mappedStandings.get(p.getTeamId()).points+=cfg.getDraw();
                        break;
                    case -1:
                        mappedStandings.get(p.getTeamId()).losses++;
                        mappedStandings.get(p.getTeamId()).points += cfg.getLoss();
                        break;
                    case 1:
                        mappedStandings.get(p.getTeamId()).wins++;
                        mappedStandings.get(p.getTeamId()).points += cfg.getWin();
                        break;
                }
            }
        }
        for (Long key : mappedStandings.keySet()) standings.add(mappedStandings.get(key));
        return standings;
    }

    @Override
    public ArrayList<SetRowItem> getSetsForMatch(Context context, long matchId) {
        ArrayList<SetRowItem> sets = new ArrayList<>();

        ArrayList<DParticipant> dparticipants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant home = null, away = null;

        for (DParticipant participant : dparticipants) {
            if (participant.getRole().equals("home")) home = participant;
            else away = participant;
        }

        ArrayList<DStat> homeSets = DAOFactory.getInstance().statDAO.getByParticipant(context, home.getId(), StatsEnum.SET);
        ArrayList<DStat> awaySets = DAOFactory.getInstance().statDAO.getByParticipant(context, away.getId(), StatsEnum.SET);

        for (int i = 0; i< homeSets.size(); i++) {
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

        for (DParticipant participant : dparticipants) {
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
                    item.getWinner(), item.getAwayScore(), item.getHomeScore(), StatsEnum.SET));
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, away.getId(),
                    item.getWinner() * -1, item.getHomeScore(), item.getAwayScore(), StatsEnum.SET));
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

        if (!m.isPlayed()) {
            return ManagersFactory.getInstance().teamsManager.getById(context, participant.getTeamId()).getPlayers();
        } else {
            ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getByParticipant(context, participant.getId(), StatsEnum.MATCH_PARTICIPATION);
            Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().playerDAO.getAllPlayers(context);
            for (DStat stat : stats) {
                players.add(new Player(allPlayers.get(stat.getPlayerId())));
            }
        }

        return players;
    }
}
