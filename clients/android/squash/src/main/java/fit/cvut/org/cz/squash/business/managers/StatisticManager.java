package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.ISquashStatisticManager;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatisticManager implements ISquashStatisticManager {
    private static final int WIN = 1;
    private static final int DRAW = 2;
    private static final int LOSS = 3;

    protected Context context;
    protected ICorePlayerManager corePlayerManager;
    protected SquashDBHelper sportDBHelper;

    public StatisticManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        this.context = context;
        this.corePlayerManager = corePlayerManager;
        this.sportDBHelper = sportDBHelper;
    }

    private int calculatePoints(int result, PointConfiguration pointConfig) {
        switch (result) {
            case WIN:
                return pointConfig.getWin();
            case DRAW:
                return pointConfig.getDraw();
            case LOSS:
                return pointConfig.getLoss();
            default:
                return 0;
        }
    }

    private void addMatchResultToStanding(int result, StandingItem standing) {
        switch (result) {
            case WIN:
                standing.wins++;
                break;
            case DRAW:
                standing.draws++;
                break;
            case LOSS:
                standing.losses++;
                break;
        }
    }

    private void orderPlayers(List<SAggregatedStats> stats) {
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

    private List<SAggregatedStats> getStats(List<Player> players, List<Tournament> tournaments) {
        if (players.isEmpty())
            return new ArrayList<>();

        Map<Long, SAggregatedStats> mappedStats = new HashMap<>();
        for (Player player : players)
            mappedStats.put(player.getId(), new SAggregatedStats(player.getName(), player.getId()));

        for (Tournament tournament : tournaments) {
            PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(tournament.getId());
            List<Match> matches = ManagerFactory.getInstance(context).matchManager.getByTournamentId(tournament.getId());
            // Tournament does not have matches yet.
            if (matches.isEmpty())
                continue;

            for (Match match : matches) {
                if (!match.isPlayed())
                    continue;

                Participant homeParticipant = null;
                Participant awayParticipant = null;
                for (Participant participant : match.getParticipants()) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        homeParticipant = participant;
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        awayParticipant = participant;
                }

                List<PlayerStat> homePlayerStats = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(homeParticipant.getId());
                for (PlayerStat playerStat : homePlayerStats) {
                    // This player is not in requested array of players.
                    if (!mappedStats.containsKey(playerStat.getPlayerId()))
                        continue;

                    SAggregatedStats currentStat = mappedStats.get(playerStat.getPlayerId());
                    currentStat.games_played++;
                    /* Add won and lost sets to Standing */
                    currentStat.setsWon += match.getHomeWonSets();
                    currentStat.setsLost += match.getAwayWonSets();

                    /* Add Win / Draw / Loss and Points to Standing */
                    if (match.getHomeWonSets() > match.getAwayWonSets()) {
                        currentStat.won++;
                        currentStat.points += calculatePoints(WIN, pointConfiguration);
                    } else if (match.getHomeWonSets() < match.getAwayWonSets()) {
                        currentStat.lost++;
                        currentStat.points += calculatePoints(LOSS, pointConfiguration);
                    } else {
                        currentStat.draws++;
                        currentStat.points += calculatePoints(DRAW, pointConfiguration);
                    }

                    /* Add Balls to Standing */
                    List<SetRowItem> sets = getMatchSets(match.getId());
                    for (SetRowItem set : sets) {
                        currentStat.ballsWon += set.getHomeScore();
                        currentStat.ballsLost += set.getAwayScore();
                    }
                }

                List<PlayerStat> awayPlayerStats = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(awayParticipant.getId());
                for (PlayerStat playerStat : awayPlayerStats) {
                    // This player is not in requested array of players.
                    if (!mappedStats.containsKey(playerStat.getPlayerId()))
                        continue;

                    SAggregatedStats currentStat = mappedStats.get(playerStat.getPlayerId());
                    currentStat.games_played++;
                    /* Add won and lost sets to Standing */
                    currentStat.setsWon += match.getAwayWonSets();
                    currentStat.setsLost += match.getHomeWonSets();

                    /* Add Win / Draw / Loss and Points to Standing */
                    if (match.getHomeWonSets() < match.getAwayWonSets()) {
                        currentStat.won++;
                        currentStat.points += calculatePoints(WIN, pointConfiguration);
                    } else if (match.getHomeWonSets() > match.getAwayWonSets()) {
                        currentStat.lost++;
                        currentStat.points += calculatePoints(LOSS, pointConfiguration);
                    } else {
                        currentStat.draws++;
                        currentStat.points += calculatePoints(DRAW, pointConfiguration);
                    }

                    /* Add Balls to Standing */
                    List<SetRowItem> sets = getMatchSets(match.getId());
                    for (SetRowItem set : sets) {
                        currentStat.ballsWon += set.getAwayScore();
                        currentStat.ballsLost += set.getHomeScore();
                    }
                }
            }
        }

        for (SAggregatedStats stat : mappedStats.values()) {
            if (stat.games_played > 0) {
                stat.matchWinRate = (double) 100 * stat.won / stat.games_played;
                stat.setsWonAvg = (double) stat.setsWon / stat.games_played;
                stat.setsLostAvg = (double) stat.setsLost/ stat.games_played;
                stat.ballsWonAvg = (double) stat.ballsWon / stat.games_played;
                stat.ballsLostAvg = (double) stat.ballsLost / stat.games_played;
            }
            if (stat.getTotalSets() > 0) {
                stat.setsWinRate = (double) 100 * stat.setsWon / stat.getTotalSets();
            }
        }

        return new ArrayList<>(mappedStats.values());
    }
    /*
    private List<SAggregatedStats> getStats(List<Player> players, List<Tournament> tournaments){
        List<SAggregatedStats> stats = new ArrayList<>();
        Map<Long, SAggregatedStats> mappedStats = new HashMap<>();
        Map<Long, ArrayList<Long>> mappedParticipants = new HashMap<>();
        Map<Long, PointConfiguration> tournamentPointConfigs = new HashMap<>();

        // Load point configurations for all tournaments
        for (Match match : matches) {
            if (!tournamentPointConfigs.containsKey(match.getTournamentId())) {
                PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(context, match.getTournamentId());
                tournamentPointConfigs.put(match.getTournamentId(), pointConfiguration);
            }
        }

        // Save players to map
        for (Player player : players)
            mappedStats.put(player.getId(), new SAggregatedStats(player.getName(), player.getId()));

        for (Match match : matches) {
            for (Participant participant : match.getParticipants()) {
                if (!mappedParticipants.containsKey(match.getParticipantId())) {
                    mappedParticipants.put(match.getParticipantId(), DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, match.getParticipantId()));
                }
            }
            for (Long playerId : mappedParticipants.get(match.getParticipantId())) {
                if (mappedStats.get(playerId) == null)
                    continue;
                mappedStats.get(playerId).games_played++;
                switch (match.getStatus()) {
                    case -1:
                        mappedStats.get(playerId).lost++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(match.getTournamentId()).getLoss();
                        break;
                    case 0:
                        mappedStats.get(playerId).draws++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(match.getTournamentId()).getDraw();
                        break;
                    case 1:
                        mappedStats.get(playerId).won++;
                        mappedStats.get(playerId).points += tournamentPointConfigs.get(match.getTournamentId()).getWin();
                        break;
                }
            }
        }
        for (DStat set : sets) {
            // TODO ? Match is not saved yet completely and the stats for them should be loaded
            if (mappedParticipants.get(set.getParticipantId()) == null) {
                continue;
            }
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
    }*/

    @Override
    public List<SAggregatedStats> getByCompetitionId(long competitionId) {
        List<Player> players = ManagerFactory.getInstance(context).competitionManager.getCompetitionPlayers(competitionId);
        List<Tournament> tournaments = ManagerFactory.getInstance(context).tournamentManager.getByCompetitionId(competitionId);

        List<SAggregatedStats> res = getStats(players, tournaments);
        orderPlayers(res);
        return res;
    }

    @Override
    public List<SAggregatedStats> getByTournamentId(long tournamentId) {
        List<Player> players = ManagerFactory.getInstance(context).tournamentManager.getTournamentPlayers(tournamentId);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(ManagerFactory.getInstance(context).tournamentManager.getById(tournamentId));
        List<SAggregatedStats> res = getStats(players, tournaments);
        orderPlayers(res);
        return res;
    }

    @Override
    public List<SetRowItem> getMatchSets(long matchId) {
        Match match = ManagerFactory.getInstance(context).matchManager.getById(matchId);
        if (!match.isPlayed())
            return new ArrayList<>();

        long homeParticipantId = 0, awayParticipantId = 0;
        for (Participant participant : match.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                homeParticipantId = participant.getId();
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                awayParticipantId = participant.getId();
            }
        }

        List<ParticipantStat> homeStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(homeParticipantId);
        List<ParticipantStat> awayStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(awayParticipantId);

        List<SetRowItem> sets = new ArrayList<>();
        for (int i=0; i < match.getSetsNumber(); i++) {
            sets.add(new SetRowItem(homeStats.get(i).getPoints(), awayStats.get(i).getPoints()));
        }
        return sets;
    }

    @Override
    public SAggregatedStats getByPlayerId(long playerId) {
        Player player = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(playerId);
        List<Player> filtered_players = new ArrayList<>();
        filtered_players.add(player);
        List<Tournament> tournaments = ManagerFactory.getInstance(context).tournamentManager.getByPlayer(playerId);
        List<SAggregatedStats> stats = getStats( filtered_players, tournaments);
        if (stats.isEmpty())
            return null;
        return stats.get(0);
    }

    @Override
    public List<SAggregatedStats> getAllAggregated(Context context) {
        return new ArrayList<>();
        /*Map<Long, Player> playerMap = ManagerFactory.getInstance(context).corePlayerManager.getAllPlayers();
        List<Player> players = new ArrayList<>();
        for (Map.Entry<Long, Player> playerEntry : playerMap.entrySet()) {
            players.add(playerEntry.getValue());
        }
        ArrayList<DStat> results = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.MATCH);
        ArrayList<DStat> sets = DAOFactory.getInstance().statDAO.getAll(context, StatsEnum.SET);

        return getStats(context, players, results, sets);*/
    }

    private void orderStandings(List<StandingItem> standings) {
        Collections.sort(standings, new Comparator<StandingItem>() {
            @Override
            public int compare(StandingItem ls, StandingItem rs) {
                if (rs.points != ls.points)
                    return rs.points - ls.points;
                if (rs.setsWon - rs.setsLost != ls.setsWon - ls.setsLost) {
                    return (rs.setsWon - rs.setsLost) - (ls.setsWon - ls.setsLost);
                }
                if (rs.setsWon != ls.setsWon) {
                    return rs.setsWon- ls.setsWon;
                }
                return ls.getMatches()-rs.getMatches();
            }
        });
    }

    @Override
    public List<StandingItem> getStandingsByTournamentId(long tournamentId) {
        Tournament tournament = ManagerFactory.getInstance(context).tournamentManager.getById(tournamentId);
        Competition competition = ManagerFactory.getInstance(context).competitionManager.getById(tournament.getCompetitionId());
        CompetitionType type = competition.getType();
        Map<Long, StandingItem> mappedStandings = new HashMap<>();

        List<Match> matches = ManagerFactory.getInstance(context).matchManager.getByTournamentId(tournamentId);
        PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(tournamentId);

        if (CompetitionTypes.individuals().equals(type)) {
            List<Player> players = ManagerFactory.getInstance(context).tournamentManager.getTournamentPlayers(tournamentId);
            for (Player player : players)
                mappedStandings.put(player.getId(), new StandingItem(player.getId(), player.getName()));
        } else {
            List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(tournamentId);
            for (Team team : teams)
                mappedStandings.put(team.getId(), new StandingItem(team.getId(), team.getName()));
        }

        for (Match match : matches) {
            if (!match.isPlayed())
                continue;

            Participant homeParticipant = null;
            Participant awayParticipant = null;
            for (Participant participant : match.getParticipants()) {
                if (ParticipantType.home.toString().equals(participant.getRole()))
                    homeParticipant = participant;
                else if (ParticipantType.away.toString().equals(participant.getRole()))
                    awayParticipant = participant;
            }

            StandingItem homeStanding = mappedStandings.get(homeParticipant.getParticipantId());
            StandingItem awayStanding = mappedStandings.get(awayParticipant.getParticipantId());

            /* Add won and lost sets to Standing */
            homeStanding.setsWon += match.getHomeWonSets();
            awayStanding.setsWon += match.getAwayWonSets();

            homeStanding.setsLost += match.getAwayWonSets();
            awayStanding.setsLost += match.getHomeWonSets();

            /* Add Win / Draw / Loss and Points to Standing */
            if (match.getHomeWonSets() > match.getAwayWonSets()) {
                addMatchResultToStanding(WIN, homeStanding);
                addMatchResultToStanding(LOSS, awayStanding);

                homeStanding.points += calculatePoints(WIN, pointConfiguration);
                awayStanding.points += calculatePoints(LOSS, pointConfiguration);
            } else if (match.getHomeWonSets() < match.getAwayWonSets()) {
                addMatchResultToStanding(WIN, awayStanding);
                addMatchResultToStanding(LOSS, homeStanding);

                awayStanding.points += calculatePoints(WIN, pointConfiguration);
                homeStanding.points += calculatePoints(LOSS, pointConfiguration);
            } else {
                addMatchResultToStanding(DRAW, homeStanding);
                addMatchResultToStanding(DRAW, awayStanding);

                awayStanding.points += calculatePoints(DRAW, pointConfiguration);
                homeStanding.points += calculatePoints(DRAW, pointConfiguration);
            }

            /* Add Balls to Standing */
            List<SetRowItem> sets = getMatchSets(match.getId());
            for (SetRowItem set : sets) {
                homeStanding.ballsWon += set.getHomeScore();
                awayStanding.ballsWon += set.getAwayScore();
                homeStanding.ballsLost += set.getAwayScore();
                awayStanding.ballsLost += set.getHomeScore();
            }
        }

        List<StandingItem> standings = new ArrayList<>(mappedStandings.values());
        orderStandings(standings);
        return standings;
    }

    public void updateStatsForMatch(long matchId, ArrayList<SetRowItem> sets) {
/*        DMatch m = DAOFactory.getInstance().matchDAO.getById(context, matchId);
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

        Tournament t = ManagerFactory.getInstance(context).tournamentManager.getById(context, m.getTournamentId());

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
                result * -1, -1, -1, StatsEnum.MATCH));*/
    }
}
