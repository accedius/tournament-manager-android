package fit.cvut.org.cz.bowling.business.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

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
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.data.helpers.WinConditionTypes;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
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
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;


public class StatisticManager extends BaseManager<AggregatedStatistics> implements IStatisticManager {
    private static final int UNKNOWN = -1;
    private static final int WIN = 1;
    private static final int DRAW = 2;
    private static final int LOSS = 3;

    @Override
    protected Class<AggregatedStatistics> getMyClass() {
        return AggregatedStatistics.class;
    }

    private int calculatePoints(int result, PointConfiguration pointConfig, Match match) {
        if(pointConfig == null) {
            return 0;
        }

        List<Float> config = pointConfig.getConfigurationPlacePoints();

        if(config.size() <= result) {
            return 0;
        }
        return config.get(result).intValue();
    }


    private int getMatchResultForParticipant(Participant participant, Match match) {
        int participant_score = 0, opponent_score = 0;
        List<Participant> matchParticipants = ((IParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(match.getId());
        for (Participant matchParticipant : matchParticipants) {
            final IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantId(matchParticipant.getId());
            if(participantStats.isEmpty())
                return UNKNOWN;
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

    private AggregatedStatistics processTournamentOfIndividualsRankedByGlobalPoints(Player player, Tournament tournament) {
        TreeMap<Long, Long> playerScores = new TreeMap<>();
        long strikes = 0, spares = 0, matchesPlayed = 0;
        final List<Match> matches = ((IMatchManager)managerFactory.getEntityManager(Match.class)).getByTournamentId(tournament.getId());
        for(Match match : matches) {

            if(!match.isValidForStats()) {
                continue;
            }

            for(Participant participant : match.getParticipants()) {

                if(participant == null) {
                    //Shouldn't happen
                    continue;
                }

                for(fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat ps : participant.getPlayerStats()) {
                    PlayerStat playerStat = (PlayerStat) ps;
                    if(ps == null) {
                        Log.d("STATISFIX", "Bad cast");
                        continue;
                    }

                    Long currentPoints = playerScores.get(playerStat.getPlayerId());

                    if(currentPoints == null) {
                        playerScores.put(playerStat.getPlayerId(), (long) playerStat.getPoints());
                    } else {
                        currentPoints += playerStat.getPoints();
                    }

                    if(playerStat.getPlayerId() == player.getId()) {
                        strikes += playerStat.getStrikes();
                        spares += playerStat.getSpares();
                        matchesPlayed++;
                    }
                }
            }
        }

        //Find the player's place and give him match points for his effort
        TreeMap<Long, List<Long>> scoreToPlayer = new TreeMap<>();

        for(Map.Entry<Long, Long> entry : playerScores.entrySet()) {
            List<Long> list = scoreToPlayer.get(entry.getValue());

            if(list == null) {
                list = new LinkedList<>();
                scoreToPlayer.put(entry.getValue(), list);
            }
            list.add(entry.getKey());
        }

        int place = scoreToPlayer.size() - 1;
        long matchPoints = 0;
        long points = 0;
        long lastPointCount = Long.MAX_VALUE;

        for(Map.Entry<Long, List<Long>> list : scoreToPlayer.entrySet()) {
            for(Long playerId : list.getValue()) {
                if(playerId == player.getId()) {
                    final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
                    List<PointConfiguration> pointConfigurations = pointConfigurationManager.getByTournamentId(tournament.getId());

                    //Find the one based on the amount of players that played in the tournament
                    PointConfiguration pointConfiguration = null;
                    for(PointConfiguration p : pointConfigurations) {
                        if(p.sidesNumber == playerScores.size()) {
                            pointConfiguration = p;
                            break;
                        }
                    }
                    matchPoints = calculatePoints(place, pointConfiguration, null);
                    points = playerScores.get(playerId);
                    break;
                }

                long pts = playerScores.get(playerId);
                Log.d("STATISFIX", "place: " + place + " goes to " + playerId + " with " + pts);
            }
            place--;
        }

        Log.d("STATISFIX", "" + playerScores.size() + " Player " + player.getId() + " in TRNMNT: " + tournament.getId() + " has " + place + " with P: " +  points + ", St: " + strikes + ", Sp: " + spares);

        //It's fine if the player in question was never in this tournament
        return new AggregatedStatistics(player.getId(), player.getName(), matchesPlayed, strikes, spares, points, matchPoints);
    }

    private AggregatedStatistics aggregateStats(Player player, List<PlayerStat> allStats) {
        final IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
        List<PlayerStat> playerStats = playerStatManager.getByPlayerId(player.getId());
        if (allStats != null) {
            playerStats = intersection(playerStats, allStats); // common elements -> players stats in competition
        }

        Set<Long> checkedTournaments = new HashSet<>();

        long matches = 0, strikes = 0, spares = 0, points = 0, matchPoints = 0;
        for(PlayerStat stat : playerStats) {
            Participant participant = managerFactory.getEntityManager(Participant.class).getById(stat.getParticipantId());

            if(participant == null) {
                continue;
            }

            Match match = managerFactory.getEntityManager(Match.class).getById(participant.getMatchId());

            if(!match.isValidForStats()) {
                continue;
            }

            Tournament tournament = managerFactory.getEntityManager(Tournament.class).getById(match.getTournamentId());

            //Use alternative evaluation if alternative winCondition is detected
            WinCondition winCondition = ((WinConditionManager)managerFactory.getEntityManager(WinCondition.class)).getByTournamentId(match.getTournamentId());
            boolean isTournamentRankedByTotalPointsAcrossMatches = winCondition != null && (winCondition.getWinCondition() == WinConditionTypes.win_condition_total_points);

            if(isTournamentRankedByTotalPointsAcrossMatches) {
                final IMatchManager matchManager = managerFactory.getEntityManager(Match.class);

                final List<Match> matchList = matchManager.getByTournamentId(tournament.getId());

                if(matchList.isEmpty() || checkedTournaments.contains(tournament.getId())) {
                    //Avoid duplicate testing
                    continue;
                }

                checkedTournaments.add(tournament.getId());

                AggregatedStatistics result = processTournamentOfIndividualsRankedByGlobalPoints(player, tournament);
                matches += result.getMatches();
                strikes += result.getStrikes();
                spares += result.getSpares();
                points += result.getPoints();
                matchPoints += result.getMatchPoints();
                continue;
            }

            boolean isMatchOfIndividuals = TournamentTypes.individuals().equals(TournamentTypes.getMyTournamentType(tournament.getTypeId()));

            if(isMatchOfIndividuals) {
                //Get all participants in this match
                List<Participant> commonMatchParticipants = ((ParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(participant.getMatchId());

                //Get all participant stats in this match
                List<PlayerStat> participantStats = new LinkedList<>();
                for(Participant p : commonMatchParticipants) {
                    participantStats.addAll(((PlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(p.getId()));
                }

                //Determine the winner
                PlayerStat winner = stat;
                int place = 0;
                for(PlayerStat s : participantStats) {
                    if(stat.getPoints() < s.getPoints()) {
                        place++;
                    }
                    if(winner.getPoints() < s.getPoints()) {
                        winner = s;
                    }
                }

                //Get the point award configuration
                final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
                List<PointConfiguration> pointConfigurations = pointConfigurationManager.getByTournamentId(match.getTournamentId());

                //Find the one based on the amount of sides in the match
                PointConfiguration pointConfiguration = null;

                for(PointConfiguration p : pointConfigurations) {
                    if(p.sidesNumber == match.getParticipants().size()) {
                        pointConfiguration = p;
                        break;
                    }
                }

                //Give team point based on place in current match
                matchPoints += calculatePoints(place, pointConfiguration, match);
            } else {
                //Get all participants in this match
                List<Participant> commonMatchParticipants = ((ParticipantManager)managerFactory.getEntityManager(Participant.class)).getByMatchId(participant.getMatchId());

                //Get all participant stats in this match
                List<PlayerStat> participantStats = new LinkedList<>();
                HashMap<Long, Long> scores = new HashMap<>();
                for(Participant p : commonMatchParticipants) {
                    participantStats.addAll(((PlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(p.getId()));
                    scores.put(p.getId(), Long.valueOf(0));
                }


                //Determine the winner
                // Score per participant + who am I?
                long me = -1;
                for(PlayerStat playerStat : participantStats) {
                    Long l = scores.get(playerStat.getParticipantId());
                    l += playerStat.getPoints();
                    scores.remove(playerStat.getParticipantId());
                    scores.put(playerStat.getParticipantId(), l);

                    if(playerStat.getPlayerId() == player.getId()) {
                        me = playerStat.getParticipantId();
                    }
                }

                // Which place is 'me' on?
                int place = 0;
                long myPoints = scores.get(me);

                for(Map.Entry<Long, Long> s : scores.entrySet()) {
                    if(myPoints < s.getValue()) {
                        place++;
                    }
                }

                //Get the point award configuration
                final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
                List<PointConfiguration> pointConfigurations = pointConfigurationManager.getByTournamentId(match.getTournamentId());

                //Find the one based on the amount of sides in the match
                PointConfiguration pointConfiguration = null;

                for(PointConfiguration p : pointConfigurations) {
                    if(p.sidesNumber == match.getParticipants().size()) {
                        pointConfiguration = p;
                        break;
                    }
                }

                //Give team point based on place in current match
                matchPoints += calculatePoints(place, pointConfiguration, match);
            }

            //Accumulate stats
            matches++;
            strikes += stat.getStrikes();
            spares += stat.getSpares();
            points += stat.getPoints();
        }

        return new AggregatedStatistics(player.getId(), player.getName(), matches, strikes, spares, points, matchPoints);
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
        //final ITeamManager teamManager = managerFactory.getEntityManager(Team.class);
        final IPointConfigurationManager pointConfigurationManager = managerFactory.getEntityManager(PointConfiguration.class);
        //List<Team> teams = teamManager.getByTournamentId(tournamentId);
        ArrayList<Standing> standings = new ArrayList<>();
        //PointConfiguration pointConfiguration = pointConfigurationManager.getById(tournamentId);
        PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();

        /*for (Team t : teams) {
            standings.add(new Standing(t.getName(), t.getId()));
        }*/
        final IMatchManager matchManager = managerFactory.getEntityManager(Match.class);
        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        for (Match match : matches) {
            if (!match.isPlayed())
                continue;

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
                if (rs.getStrikes() != ls.getStrikes()) {
                    return (int)(rs.getStrikes()- ls.getStrikes());
                }
                return (int)(ls.getMatches()-rs.getMatches());
            }
        });
    }
}
