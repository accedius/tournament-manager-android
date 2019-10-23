package fit.cvut.org.cz.bowling.business.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.bowling.business.entities.Standing;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;


public class StatisticManager extends BaseManager<AggregatedStatistics> implements IStatisticManager {
    private static final int WIN = 1;
    private static final int DRAW = 2;
    private static final int LOSS = 3;

    @Override
    protected Class<AggregatedStatistics> getMyClass() {
        return AggregatedStatistics.class;
    }

    private int calculatePoints(int result, PointConfiguration pointConfig, Match match) {
        switch (result) {
            case WIN:
                if (match.isShootouts())
                    //return pointConfig.soW;
                    return 1;
                if (match.isOvertime())
                    //return pointConfig.otW;
                    return 1;
                //return pointConfig.ntW;
            case DRAW:
                if (match.isOvertime())
                    return 1;
                    //return pointConfig.otD;
                return 1;
                //return pointConfig.ntD;
            case LOSS:
                if (match.isShootouts())
                    return 1;
                    //return pointConfig.soL;
                if (match.isOvertime())
                    return 1;
                    //return pointConfig.otL;
                return 1;
                //eturn pointConfig.ntL;
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
        List<Participant> matchParticipants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(match.getId());
        for (Participant matchParticipant : matchParticipants) {
            final IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantId(matchParticipant.getId());
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
        final IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
        List<PlayerStat> playerStats = playerStatManager.getByPlayerId(player.getId());
        if (allStats != null) {
            playerStats = intersection(playerStats, allStats); // common elements -> players stats in competition
        }
        long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0, saves = 0;
        for (PlayerStat stat : playerStats) {
            Participant participant = managerFactory.getEntityManager(Participant.class).getById(stat.getParticipantId());
            Match match = managerFactory.getEntityManager(Match.class).getById(participant.getMatchId());
            if (!match.isPlayed())
                continue;

            goals += stat.getGoals();
            assists += stat.getAssists();
            plusMinusPoints += stat.getPlusMinus();
            saves += stat.getSaves();
            matches++;

            // Count team points, win, and other...
            int result = getMatchResultForParticipant(participant, match);
            final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
            PointConfiguration pointConfiguration = pointConfigurationManager.getById(match.getTournamentId());
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
        //long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0, saves = 0;
        return new AggregatedStatistics(player.getId(), player.getName(), matches, wins, draws, losses, goals, assists, plusMinusPoints, teamPoints, saves);
    }

    @Override
    public AggregatedStatistics getByPlayerId(long playerId) {
        ArrayList<PlayerStat> allStats = null;
        Player player = managerFactory.getEntityManager(Player.class).getById(playerId);
        AggregatedStatistics res = aggregateStats(player, allStats);
        return res;
    }

    private List<PlayerStat> getStatsByCompetitionId(long competitionId) {
        List<PlayerStat> playerStats = new ArrayList<>();
        List<Tournament> tournaments = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getByCompetitionId(competitionId);
        List<Match> matches = new ArrayList<>();
        for (Tournament tournament : tournaments)
            matches.addAll(((IMatchManager)managerFactory.getEntityManager(Match.class)).getByTournamentId(tournament.getId()));

        List<Participant> participants = new ArrayList<>();
        for (Match match : matches)
            participants.addAll(((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(match.getId()));

        for (Participant participant : participants) {
            List<PlayerStat> participantPlayerStats = ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
            playerStats.addAll(participantPlayerStats);
        }

        return playerStats;
    }

    @Override
    public ArrayList<AggregatedStatistics> getByCompetitionId(long competitionId) {
        final ICompetitionManager competitionManager = managerFactory.getEntityManager(Competition.class);
        List<Player> competitionPlayers = competitionManager.getCompetitionPlayers(competitionId);
        List<PlayerStat> competitionStats = getStatsByCompetitionId(competitionId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for (Player player : competitionPlayers)
            res.add(aggregateStats(player, competitionStats));

        orderPlayers(res);
        return res;
    }

    private List<PlayerStat> getStatsByTournamentId(long tournamentId) {
        final IMatchManager matchManager = managerFactory.getEntityManager(Match.class);
        List<PlayerStat> playerStats = new ArrayList<>();
        List<Match> matches = new ArrayList<>(matchManager.getByTournamentId(tournamentId));

        List<Participant> participants = new ArrayList<>();
        for (Match match : matches)
            participants.addAll(((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(match.getId()));

        for (Participant participant : participants) {
            List<PlayerStat> participantPlayerStats = ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
            playerStats.addAll(participantPlayerStats);
        }

        return playerStats;
    }

    @Override
    public List<AggregatedStatistics> getByTournamentId(long tournamentId) {
        final ITournamentManager tournamentManager = managerFactory.getEntityManager(Tournament.class);
        List<Player> tournamentPlayers = tournamentManager.getTournamentPlayers(tournamentId);
        List<PlayerStat> tournamentStats = getStatsByTournamentId(tournamentId);
        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for (Player player : tournamentPlayers)
            res.add(aggregateStats(player, tournamentStats));

        orderPlayers(res);
        return res;
    }

    @Override
    public List<Standing> getStandingsByTournamentId(long tournamentId) {
        final ITeamManager teamManager = managerFactory.getEntityManager(Team.class);
        final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
        List<Team> teams = teamManager.getByTournamentId(tournamentId);
        ArrayList<Standing> standings = new ArrayList<>();
        //PointConfiguration pointConfiguration = pointConfigurationManager.getById(tournamentId);
        PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();

        for (Team t : teams) {
            standings.add(new Standing(t.getName(), t.getId()));
        }
        final IMatchManager matchManager = managerFactory.getEntityManager(Match.class);
        List<Match> matches = matchManager.getByTournamentId(tournamentId);
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

    public static void orderStandings(List<Standing> standings) {
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
}
