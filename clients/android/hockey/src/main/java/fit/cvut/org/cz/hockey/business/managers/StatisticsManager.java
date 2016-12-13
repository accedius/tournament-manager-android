package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IHockeyStatisticsManager;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager implements IHockeyStatisticsManager {
    private static final int WIN = 1;
    private static final int DRAW = 2;
    private static final int LOSS = 3;

    protected Context context;
    protected ICorePlayerManager corePlayerManager;
    protected HockeyDBHelper sportDBHelper;

    public StatisticsManager(Context context, ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        this.context = context;
        this.corePlayerManager = corePlayerManager;
        this.sportDBHelper = sportDBHelper;
    }

    private int calculatePoints(int result, PointConfiguration pointConfig, Match match) {
        switch (result) {
            case WIN:
                if (match.isShootouts())
                    return pointConfig.soW;
                if (match.isOvertime())
                    return pointConfig.otW;
                return pointConfig.ntW;
            case DRAW:
                if (match.isOvertime())
                    return pointConfig.otD;
                return pointConfig.ntD;
            case LOSS:
                if (match.isShootouts())
                    return pointConfig.soL;
                if (match.isOvertime())
                    return pointConfig.otL;
                return pointConfig.ntL;
            default:
                return 0;
        }
    }

    private void addMatchResultToStanding(int result, Standing standing, Match match) {
        switch (result) {
            case WIN:
                if (match.isShootouts()) {
                    standing.addWinSo();
                    break;
                }
                if (match.isOvertime()) {
                    standing.addWinOt();
                    break;
                }
                standing.addWin();
                break;
            case DRAW:
                standing.addDraw();
                break;
            case LOSS:
                if (match.isShootouts()) {
                    standing.addLossSo();
                    break;
                }
                if (match.isOvertime()) {
                    standing.addLossOt();
                    break;
                }
                standing.addLoss();
                break;
        }
    }

    private int getMatchResultForParticipant(Participant participant, Match match) {
        int participant_score = 0, opponent_score = 0;
        List<Participant> matchParticipants = ManagerFactory.getInstance(context).participantManager.getByMatchId(match.getId());
        for (Participant matchParticipant : matchParticipants) {
            List<ParticipantStat> participantStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(matchParticipant.getId());
            if (matchParticipant.getId() == participant.getId())
                participant_score = participantStats.get(0).getScore();
            else
                opponent_score = participantStats.get(0).getScore();
        }
        if (participant_score > opponent_score)
            return WIN;
        else if (participant_score < opponent_score)
            return LOSS;
        else
            return DRAW;
    }

    private List<PlayerStat> intersection(List<PlayerStat> stats1, List<PlayerStat> stats2) {
        List<PlayerStat> intersection = new ArrayList<>();
        for (PlayerStat stat1 : stats1) {
            if (stats2.contains(stat1))
                intersection.add(stat1);
        }
        return intersection;
    }

    private AggregatedStatistics aggregateStats(Player player, List<PlayerStat> allStats) {
        List<PlayerStat> playerStats = ManagerFactory.getInstance(context).playerStatManager.getByPlayerId(player.getId());
        if (allStats != null) {
            playerStats = intersection(playerStats, allStats); // common elements -> players stats in competition
        }
        long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0, saves = 0;
        for (PlayerStat stat : playerStats) {
            Participant participant = ManagerFactory.getInstance(context).participantManager.getById(stat.getParticipantId());
            Match match = ManagerFactory.getInstance(context).matchManager.getById(participant.getMatchId());
            if (!match.isPlayed())
                continue;

            goals += stat.getGoals();
            assists += stat.getAssists();
            plusMinusPoints += stat.getPlusMinus();
            saves += stat.getSaves();
            matches++;

            // Count team points, win, and other...
            int result = getMatchResultForParticipant(participant, match);
            PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(match.getTournamentId());
            teamPoints += calculatePoints(result, pointConfiguration, match);
            switch (result) {
                case WIN:
                    wins++;
                    break;
                case DRAW:
                    draws++;
                    break;
                case LOSS:
                    losses++;
                    break;
            }
        }
        return new AggregatedStatistics(player.getId(), player.getName(), matches, wins, draws, losses, goals, assists, plusMinusPoints, teamPoints, saves);
    }

    @Override
    public List<AggregatedStatistics> getAllAggregated() {
        List<PlayerStat> allStats = null;
        Collection<Player> players = corePlayerManager.getAllPlayers().values();

        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for (Player player : players) {
            res.add(aggregateStats(player, allStats));
        }
        return res;
    }

    @Override
    public AggregatedStatistics getByPlayerId(long playerId) {
        ArrayList<PlayerStat> allStats = null;
        Player player = corePlayerManager.getPlayerById(playerId);
        AggregatedStatistics res = aggregateStats(player, allStats);
        return res;
    }

    private List<PlayerStat> getStatsByCompetitionId(long competitionId) {
        List<PlayerStat> playerStats = new ArrayList<>();
        List<Tournament> tournaments = ManagerFactory.getInstance(context).tournamentManager.getByCompetitionId(competitionId);
        List<Match> matches = new ArrayList<>();
        for (Tournament tournament : tournaments)
            matches.addAll(ManagerFactory.getInstance(context).matchManager.getByTournamentId(tournament.getId()));

        List<Participant> participants = new ArrayList<>();
        for (Match match : matches)
            participants.addAll(ManagerFactory.getInstance(context).participantManager.getByMatchId(match.getId()));

        for (Participant participant : participants) {
            List<PlayerStat> participantPlayerStats = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId());
            playerStats.addAll(participantPlayerStats);
        }

        return playerStats;
    }

    @Override
    public ArrayList<AggregatedStatistics> getByCompetitionId(long competitionId) {
        List<Player> competitionPlayers = ManagerFactory.getInstance(context).competitionManager.getCompetitionPlayers(competitionId);
        List<PlayerStat> competitionStats = getStatsByCompetitionId(competitionId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for (Player player : competitionPlayers)
            res.add(aggregateStats(player, competitionStats));

        orderPlayers(res);
        return res;
    }

    private List<PlayerStat> getStatsByTournamentId(long tournamentId) {
        List<PlayerStat> playerStats = new ArrayList<>();
        List<Match> matches = new ArrayList<>(ManagerFactory.getInstance(context).matchManager.getByTournamentId(tournamentId));

        List<Participant> participants = new ArrayList<>();
        for (Match match : matches)
            participants.addAll(ManagerFactory.getInstance(context).participantManager.getByMatchId(match.getId()));

        for (Participant participant : participants) {
            List<PlayerStat> participantPlayerStats = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId());
            playerStats.addAll(participantPlayerStats);
        }

        return playerStats;
    }

    @Override
    public List<AggregatedStatistics> getByTournamentId(long tournamentId) {
        List<Player> tournamentPlayers = ManagerFactory.getInstance(context).tournamentManager.getTournamentPlayers(tournamentId);
        List<PlayerStat> tournamentStats = getStatsByTournamentId(tournamentId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for (Player player : tournamentPlayers)
            res.add(aggregateStats(player, tournamentStats));

        orderPlayers(res);
        return res;
    }

    @Override
    public List<Standing> getStandingsByTournamentId(long tournamentId) {
        List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(tournamentId);
        ArrayList<Standing> standings = new ArrayList<>();
        PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(tournamentId);

        for (Team t : teams) {
            standings.add(new Standing(t.getName(), t.getId()));
        }
        List<Match> matches = ManagerFactory.getInstance(context).matchManager.getByTournamentId(tournamentId);
        for (Match match : matches) {
            if (!match.isPlayed())
                continue;

            Standing standingH = null;
            Standing standingA = null;
            for (Standing standing : standings) {
                if (standing.getTeamId() == match.getHomeParticipantId())
                    standingH = standing;
                else if (standing.getTeamId() == match.getAwayParticipantId())
                    standingA = standing;
            }

            if (standingA == null || standingH == null)
                continue;

            standingH.addGoalsGiven(match.getHomeScore());
            standingH.addGoalsReceived(match.getAwayScore());

            standingA.addGoalsGiven(match.getAwayScore());
            standingA.addGoalsReceived(match.getHomeScore());

            for (Participant participant : match.getParticipants()) {
                int result = getMatchResultForParticipant(participant, match);
                int points = calculatePoints(result, pointConfiguration, match);
                Standing actualStanding;
                if (standingH.getTeamId() == participant.getParticipantId()) {
                    actualStanding = standingH;
                } else {
                    actualStanding = standingA;
                }
                actualStanding.addPoints(points);
                addMatchResultToStanding(result, actualStanding, match);
            }
        }
        orderStandings(standings);
        return standings;
    }

    private void orderStandings(List<Standing> standings) {
        Collections.sort(standings, new Comparator<Standing>() {
            @Override
            public int compare(Standing ls, Standing rs) {
                if (rs.getPoints() != ls.getPoints())
                    return rs.getPoints() - ls.getPoints();
                if (rs.getGoalsGiven() - rs.getGoalsReceived() != ls.getGoalsGiven() - ls.getGoalsReceived()) {
                    return (rs.getGoalsGiven() - rs.getGoalsReceived()) - (ls.getGoalsGiven() - ls.getGoalsReceived());
                }
                if (rs.getGoalsGiven() != ls.getGoalsGiven()) {
                    return rs.getGoalsGiven()- ls.getGoalsGiven();
                }
                return ls.getMatches()-rs.getMatches();
            }
        });
    }

    private void orderPlayers(List<AggregatedStatistics> stats) {
        Collections.sort(stats, new Comparator<AggregatedStatistics>() {
            @Override
            public int compare(AggregatedStatistics ls, AggregatedStatistics rs) {
                if (rs.getPoints() != ls.getPoints())
                    return (int)(rs.getPoints() - ls.getPoints());
                if (rs.getGoals() != ls.getGoals()) {
                    return (int)(rs.getGoals()- ls.getGoals());
                }
                return (int)(ls.getMatches()-rs.getMatches());
            }
        });
    }

    private long getParticipantIdByPlayerAndMatch(long playerId, long matchId) {
        /*ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(matchId);
        long partId = -1;
        for (DParticipant dParticipant : participants) {
            if (DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(dParticipant.getId()).contains(playerId)) {
                partId = dParticipant.getId();
                break;
            }
        }
        return partId;*/
        return -1;
    }

    @Override
    public MatchPlayerStatistic getPlayerStatsInMatch(long playerId, long matchId) {
        return null;/*
        MatchPlayerStatistic res = new MatchPlayerStatistic();

        long partId = getParticipantIdByPlayerAndMatch(playerId, matchId);

        List<Player> players = ManagerFactory.getInstance(context).packagePlayerManager.getPlayersByParticipant(partId);
        Player curPlayer = null;
        for (Player p : players)
            if (p.getId() == playerId)
                curPlayer = p;

        if (curPlayer == null)
            return null;

        ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(partId);
        res.setPlayerId(playerId);
        res.setName(curPlayer.getName());

        for (DStat stat : partStats) {
            if (stat.getPlayerId() != playerId)
                continue;

            int value = Integer.parseInt(stat.getValue());
            switch (StatsEnum.valueOf(stat.getStatsEnumId())) {
                case goals:
                    res.setGoals(value);
                    break;
                case assists:
                    res.setAssists(value);
                    break;
                case plus_minus_points:
                    res.setPlusMinusPoints(value);
                    break;
                case saves:
                    res.setSaves(value);
                    break;
                default:
                    break;
            }
        }
        return res;*/
    }

    @Override
    public void updatePlayersInMatch(long matchId, ParticipantType parType, List<Long> playerIds) {
        /*ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(matchId);
        DParticipant currentPart = null;
        for (DParticipant part : participants) {
            if (part.getRole().equals(parType.toString())) {
                currentPart = part;
                break;
            }
        }
        if (currentPart == null)
            return;

        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers();
        ArrayList<Player> playerListToUpdate = new ArrayList<>();
        for (Long pId : playerIds) {
            playerListToUpdate.add(allPlayers.get(pId));
        }

        long tourId = factory.matchManager.getById(matchId).getTournamentId();
        long compId = factory.tournamentManager.getById(tourId).getCompetitionId();

        ManagerFactory.getInstance(context).packagePlayerManager.updatePlayersInParticipant(currentPart.getId(), compId, tourId, playerListToUpdate);
*/
    }

    @Override
    public void updatePlayerStatsInMatch(MatchPlayerStatistic statistic, long matchId) {
        /*long partId = getParticipantIdByPlayerAndMatch(statistic.getPlayerId(), matchId);
        ArrayList<DStat> participantStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(partId);

        for (DStat stat : participantStats) {
            if (stat.getPlayerId() != statistic.getPlayerId())
                continue;

            switch (StatsEnum.valueOf(stat.getStatsEnumId())) {
                case goals:
                    stat.setValue(String.valueOf(statistic.getGoals()));
                    break;
                case assists:
                    stat.setValue(String.valueOf(statistic.getAssists()));
                    break;
                case plus_minus_points:
                    stat.setValue(String.valueOf(statistic.getPlusMinusPoints()));
                    break;
                case saves:
                    stat.setValue(String.valueOf(statistic.getSaves()));
                    break;
                default:
                    break;
            }
            DAOFactory.getInstance().statDAO.update(stat);
        }*/
    }

}
