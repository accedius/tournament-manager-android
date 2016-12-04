package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.business.interfaces.IHockeyStatisticsManager;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager implements IHockeyStatisticsManager {
    private ICorePlayerManager corePlayerManager;
    private HockeyDBHelper sportDBHelper;

    public StatisticsManager(ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        this.corePlayerManager = corePlayerManager;
        this.sportDBHelper = sportDBHelper;
    }

    private long calculatePoints(int outcome, PointConfiguration pointConfig, DMatchStat matchStat) {
        switch (outcome) {
            case 1:
                if (matchStat.isShootouts())
                    return pointConfig.soW;
                if (matchStat.isOvertime())
                    return pointConfig.otW;
                return pointConfig.ntW;
            case 2:
                if (matchStat.isOvertime())
                    return pointConfig.otD;
                return pointConfig.ntD;
            case 3:
                if (matchStat.isShootouts())
                    return pointConfig.soL;
                if (matchStat.isOvertime())
                    return pointConfig.otL;
                return pointConfig.ntL;
            default:
                return 0;
        }
    }

    private AggregatedStatistics aggregateStats(Context context, long plId, String pName, ArrayList<DStat> allStats) {
        ArrayList<DStat> playerStats = DAOFactory.getInstance().statDAO.getStatsByPlayerId(context, plId);
        if (allStats != null) {
            playerStats.retainAll(allStats); //common elements -> players stats in competition
        }
        long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0, saves = 0;
        for (DStat stat : playerStats) {
            long value = Long.parseLong(stat.getValue());
            switch (StatsEnum.valueOf(stat.getStatsEnumId())) {
                case goals:
                    goals += value;
                    break;
                case participates:
                    matches++;
                    break;
                case assists:
                    assists += value;
                    break;
                case plus_minus_points:
                    plusMinusPoints += value;
                    break;
                case saves:
                    saves += value;
                    break;
                case outcome:
                    PointConfiguration pointConfiguration = null;
                    try {
                        pointConfiguration = sportDBHelper.getHockeyPointConfigurationDAO().queryForId(stat.getTournamentId());
                        DParticipant participant = DAOFactory.getInstance().participantDAO.getById(context, stat.getParticipantId());
                        DMatchStat matchStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(context, participant.getMatchId());

                        teamPoints += calculatePoints((int) value, pointConfiguration, matchStat);
                        switch ((int) value) {
                            case 1:
                                wins++;
                                break;
                            case 2:
                                draws++;
                                break;
                            case 3:
                                losses++;
                                break;
                            default:
                                break;
                        }
                    } catch (SQLException e) {}
                    break;
            }
        }

        return new AggregatedStatistics(plId, pName, matches, wins, draws, losses, goals, assists, plusMinusPoints, teamPoints, saves);
    }

    public List<AggregatedStatistics> getAllAggregated(Context context) {
        List<DStat> allStats = null;
        //List<Player> players = ManagerFactory.getInstance(context).packagePlayerManager.getAllPlayers(context);

        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        //for (Player p : players) {
            //res.add(aggregateStats(context, p.getId(), p.getName(), allStats));
        //}
        return res;
    }

    public AggregatedStatistics getByPlayerID(Context context, long playerID) {
        ArrayList<DStat> allStats = null;
        //Player p = ManagerFactory.getInstance(context).packagePlayerManager.getPlayerById(context, playerID);
        //AggregatedStatistics res = aggregateStats(context, p.getId(), p.getName(), allStats) ;
        AggregatedStatistics res = new AggregatedStatistics(playerID, "Name", 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return res;
    }

    public ArrayList<AggregatedStatistics> getByCompetitionID(Context context, long compId) {
        //List<Player> compPlayers = ManagerFactory.getInstance(context).competitionManager.getCompetitionPlayers(context, compId);
        //ArrayList<DStat> competitionStats = DAOFactory.getInstance().statDAO.getStatsByCompetitionId(context, compId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        //for (Player p : compPlayers)
            //res.add(aggregateStats(context, p.getId(), p.getName(), competitionStats));
            // TODO
//            res.add(new AggregatedStatistics(p.getId(), p.getName(), 3, 2, 1, 0, 4, 3, -7, 10, 5));

//        orderPlayers(res);
        return res;
    }

    public List<AggregatedStatistics> getByTournamentID(Context context, long tourId) {
        //List<Player> tourPlayers = ManagerFactory.getInstance(context).packagePlayerManager.getPlayersByTournament(context, tourId);
        //ArrayList<DStat> tournamentStats = DAOFactory.getInstance().statDAO.getStatsByTournamentId(context, tourId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

//        for (Player p : tourPlayers)
            //res.add(aggregateStats(context, p.getId(), p.getName(), tournamentStats));
            // TODO
//            res.add(new AggregatedStatistics(p.getId(), p.getName(), 3, 2, 1, 0, 4, 3, -7, 10, 5));

//        orderPlayers(res);
        return res;
    }


    public List<Standing> getStandingsByTournamentId(Context context, long tourId) {
        return new ArrayList<>();
    }
    // TODO implement
//    public List<Standing> getStandingsByTournamentId(Context context, long tourId) {
//        return new ArrayList<>();
//        List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(context, tourId);
//        ArrayList<Standing> standings = new ArrayList<>();
//        PointConfiguration pointConfiguration = factory.pointConfigManager.getByTournamentId(context, tourId);
//
//        for (Team t : teams) {
//            standings.add(new Standing(t.getName(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, t.getId()));
//        }
//        List<Match> matches = factory.matchManager.getByTournamentId(context, tourId);
//        for (Match match : matches) {
//            if (!match.isPlayed())
//                continue;
//
//            Standing standingH = null;
//            Standing standingA = null;
//            for (Standing s : standings) {
//                // // TODO: 30.11.2016 set participants
//                /*if (s.getTeamId() == match.getHomeParticipantId())
//                    standingH = s;
//                else if (s.getTeamId() == match.getAwayParticipantId())
//                    standingA = s;*/
//            }
//
//            if (standingA == null || standingH == null)
//                continue;
//
//            DMatchStat matchStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(context, match.getId());
//            int homeOutcome, awayOutcome;
//
//            // TODO set home and away score
//            /*if (match.getHomeScore() > match.getAwayScore()) {
//                homeOutcome = 1;
//                awayOutcome = 3;
//                if (matchStat.isShootouts()) {
//                    standingH.addWinSo();
//                    standingA.addLossSo();;
//                } else if (matchStat.isOvertime()) {
//                    standingH.addWinOt();
//                    standingA.addLossOt();
//                } else {
//                    standingH.addWin();
//                    standingA.addLoss();
//                }
//            } else if (match.getHomeScore() == match.getAwayScore()) {
//                homeOutcome = 2;
//                standingH.addDraw();
//                awayOutcome = 2;
//                standingA.addDraw();
//            } else{*/
//                homeOutcome = 3;
//                awayOutcome = 1;
//                if (matchStat.isShootouts()) {
//                    standingH.addLossSo();
//                    standingA.addWinSo();
//                } else if (matchStat.isOvertime()) {
//                    standingH.addLossOt();
//                    standingA.addWinOt();
//                } else {
//                    standingH.addLoss();
//                    standingA.addWin();
//                }
//            /*}*/
//            standingH.addPoints(calculatePoints(homeOutcome, pointConfiguration, matchStat));
//            standingA.addPoints(calculatePoints(awayOutcome, pointConfiguration, matchStat));
///*
//            standingH.addGoalsGiven(match.getHomeScore());
//            standingA.addGoalsGiven(match.getAwayScore());
//
//            standingH.addGoalsReceived(match.getAwayScore());
//            standingA.addGoalsReceived(match.getHomeScore());*/
//        }
//        return standings;
//    }
    public Match getMatchScoreByMatchId(Context context, long id) {
        return null;
/*        DMatchStat stat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(context, id);
        if (stat == null)
            return null;

        Match score = new Match();
        score.setId(stat.getMatchId());
        score.setShootouts(stat.isShootouts());
        score.setOvertime(stat.isOvertime());
        return score;*/
    }

    public void setMatchScoreByMatchId(Context context, long matchId, Match score) {
        return;
    }
    // TODO: 4.12.2016 implement
//    public void setMatchScoreByMatchId(Context context, long matchId, Match score) {
//        DMatchStat matchStat = new DMatchStat(matchId, score.isOvertime(), score.isShootouts());
//        DAOFactory.getInstance().matchStatisticsDAO.update(context, matchStat);
//
//        int homeOutcome = 0, awayOutcome = 0;
//        /*if (score.getHomeScore() > score.getAwayScore()) {
//            homeOutcome = 1;
//            awayOutcome = 3;
//        } else if (score.getHomeScore() == score.getAwayScore()) {
//            homeOutcome = 2;
//            awayOutcome = 2;
//        } else{*/
//            homeOutcome = 3;
//            awayOutcome = 1;
//        //}
//        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
//        DStat homeScoreStat = new DStat();
//        DStat awayScoreStat = new DStat();
//        for (DParticipant dp : participants) {
//            ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, dp.getId());
//            if (dp.getRole().equals(ParticipantType.home.toString())) {
//                for (DStat ds : stats)
//                    if (StatsEnum.team_goals.toString().equals(ds.getStatsEnumId())) {
//                        homeScoreStat = ds;
//                        break;
//                    }
////                homeScoreStat.setValue(Integer.toString(score.getHomeScore()));
//                giveOutcome(context, homeOutcome, stats);
//            }
//            else if (dp.getRole().equals(ParticipantType.away.toString())) {
//                for (DStat ds : stats)
//                    if (StatsEnum.team_goals.toString().equals(ds.getStatsEnumId())) {
//                        awayScoreStat = ds; break;
//                    }
////                awayScoreStat.setValue(Integer.toString(score.getAwayScore()));
//                giveOutcome(context, awayOutcome, stats);
//            }
//        }
//        DAOFactory.getInstance().statDAO.update(context, homeScoreStat);
//        DAOFactory.getInstance().statDAO.update(context, awayScoreStat);
//    }

    private void orderPlayers(ArrayList<AggregatedStatistics> stats) {
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

    private void giveOutcome(Context context, int outcome, ArrayList<DStat> participantStats) {
        for (DStat stat : participantStats) {
            if (StatsEnum.outcome.toString().equals(stat.getStatsEnumId())) {
                stat.setValue(String.valueOf(outcome));
                DAOFactory.getInstance().statDAO.update(context, stat);
            }
        }
    }

    private long getParticipantIdByPlayerAndMatch(Context context, long playerId, long matchId) {
        /*ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        long partId = -1;
        for (DParticipant dParticipant : participants) {
            if (DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, dParticipant.getId()).contains(playerId)) {
                partId = dParticipant.getId();
                break;
            }
        }
        return partId;*/
        return -1;
    }

    public MatchPlayerStatistic getPlayerStatsInMatch(Context context, long playerId, long matchId) {
        return null;/*
        MatchPlayerStatistic res = new MatchPlayerStatistic();

        long partId = getParticipantIdByPlayerAndMatch(context, playerId, matchId);

        List<Player> players = ManagerFactory.getInstance(context).packagePlayerManager.getPlayersByParticipant(context, partId);
        Player curPlayer = null;
        for (Player p : players)
            if (p.getId() == playerId)
                curPlayer = p;

        if (curPlayer == null)
            return null;

        ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, partId);
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
    public void updatePlayersInMatch(Context context, long matchId, ParticipantType parType, List<Long> playerIds) {
        /*ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant currentPart = null;
        for (DParticipant part : participants) {
            if (part.getRole().equals(parType.toString())) {
                currentPart = part;
                break;
            }
        }
        if (currentPart == null)
            return;

        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> playerListToUpdate = new ArrayList<>();
        for (Long pId : playerIds) {
            playerListToUpdate.add(allPlayers.get(pId));
        }

        long tourId = factory.matchManager.getById(context, matchId).getTournamentId();
        long compId = factory.tournamentManager.getById(context, tourId).getCompetitionId();

        ManagerFactory.getInstance(context).packagePlayerManager.updatePlayersInParticipant(context, currentPart.getId(), compId, tourId, playerListToUpdate);
*/
    }

    public void updatePlayerStatsInMatch(Context context, MatchPlayerStatistic statistic, long matchId) {
        /*long partId = getParticipantIdByPlayerAndMatch(context, statistic.getPlayerId(), matchId);
        ArrayList<DStat> participantStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, partId);

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
            DAOFactory.getInstance().statDAO.update(context, stat);
        }*/
    }

}
