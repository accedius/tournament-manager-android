package fit.cvut.org.cz.squash.business.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.squash.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.TManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatisticManager extends TManager<SAggregatedStats> implements IStatisticManager {
    private static final int WIN = 1;
    private static final int DRAW = 2;
    private static final int LOSS = 3;

    @Override
    protected Class<SAggregatedStats> getMyClass() {
        return SAggregatedStats.class;
    }

    private List<SAggregatedStats> getStats(List<Player> players, List<Tournament> tournaments) {
        if (players.isEmpty())
            return new ArrayList<>();

        Map<Long, SAggregatedStats> mappedStats = new HashMap<>();
        for (Player player : players)
            mappedStats.put(player.getId(), new SAggregatedStats(player.getName(), player.getId()));

        for (Tournament tournament : tournaments) {
            PointConfiguration pointConfiguration = managerFactory.getEntityManager(PointConfiguration.class).getById(tournament.getId());
            List<Match> matches = ((IMatchManager)managerFactory.getEntityManager(Match.class)).getByTournamentId(tournament.getId());
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

                List<PlayerStat> homePlayerStats = ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(homeParticipant.getId());
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

                List<PlayerStat> awayPlayerStats = ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).getByParticipantId(awayParticipant.getId());
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

    @Override
    public List<SAggregatedStats> getByCompetitionId(long competitionId) {
        List<Player> players = ((ICompetitionManager)managerFactory.getEntityManager(Competition.class)).getCompetitionPlayers(competitionId);
        List<Tournament> tournaments = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getByCompetitionId(competitionId);

        List<SAggregatedStats> res = getStats(players, tournaments);
        orderPlayers(res);
        return res;
    }

    @Override
    public List<SAggregatedStats> getByTournamentId(long tournamentId) {
        List<Player> players = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getTournamentPlayers(tournamentId);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(managerFactory.getEntityManager(Tournament.class).getById(tournamentId));
        List<SAggregatedStats> res = getStats(players, tournaments);
        orderPlayers(res);
        return res;
    }

    @Override
    public List<SetRowItem> getMatchSets(long matchId) {
        Match match = managerFactory.getEntityManager(Match.class).getById(matchId);
        if (match == null || !match.isPlayed())
            return new ArrayList<>();

        long homeParticipantId = 0, awayParticipantId = 0;
        for (Participant participant : match.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                homeParticipantId = participant.getId();
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                awayParticipantId = participant.getId();
            }
        }

        List<ParticipantStat> homeStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(homeParticipantId);
        List<ParticipantStat> awayStats = ((IParticipantStatManager)managerFactory.getEntityManager(ParticipantStat.class)).getByParticipantId(awayParticipantId);

        List<SetRowItem> sets = new ArrayList<>();
        for (int i=0; i < match.getSetsNumber(); i++) {
            sets.add(new SetRowItem(homeStats.get(i).getPoints(), awayStats.get(i).getPoints()));
        }
        return sets;
    }

    @Override
    public SAggregatedStats getByPlayerId(long playerId) {
        Player player = managerFactory.getEntityManager(Player.class).getById(playerId);
        List<Player> filtered_players = new ArrayList<>();
        filtered_players.add(player);
        List<Tournament> tournaments = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getByPlayer(playerId);
        List<SAggregatedStats> stats = getStats( filtered_players, tournaments);
        if (stats.isEmpty())
            return null;
        return stats.get(0);
    }

    @Override
    public List<StandingItem> getStandingsByTournamentId(long tournamentId) {
        Tournament tournament = managerFactory.getEntityManager(Tournament.class).getById(tournamentId);
        Competition competition = managerFactory.getEntityManager(Competition.class).getById(tournament.getCompetitionId());
        CompetitionType type = competition.getType();
        Map<Long, StandingItem> mappedStandings = new HashMap<>();

        List<Match> matches = ((IMatchManager)managerFactory.getEntityManager(Match.class)).getByTournamentId(tournamentId);
        PointConfiguration pointConfiguration = managerFactory.getEntityManager(PointConfiguration.class).getById(tournamentId);

        if (CompetitionTypes.individuals().equals(type)) {
            List<Player> players = ((ITournamentManager)managerFactory.getEntityManager(Tournament.class)).getTournamentPlayers(tournamentId);
            for (Player player : players)
                mappedStandings.put(player.getId(), new StandingItem(player.getId(), player.getName()));
        } else {
            List<Team> teams = ((ITeamManager)managerFactory.getEntityManager(Team.class)).getByTournamentId(tournamentId);
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
}
