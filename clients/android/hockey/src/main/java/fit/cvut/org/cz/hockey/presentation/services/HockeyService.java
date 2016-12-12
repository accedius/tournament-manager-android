package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.hockey.business.serialization.MatchSerializer;
import fit.cvut.org.cz.hockey.business.serialization.TeamSerializer;
import fit.cvut.org.cz.hockey.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.ConflictCreator;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.stats.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Exported service for core
 * Created by atgot_000 on 1. 5. 2016.
 */
public class HockeyService extends AbstractIntentServiceWProgress {
    public HockeyService() {
        super("Hockey Service");
    }

    @Override
    protected String getActionKey() {
        return CrossPackageCommunicationConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE);
        String sportContext = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);
        ((HockeyPackage) getApplicationContext()).setSportContext(sportContext);

        switch (action) {
            case CrossPackageCommunicationConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                AggregatedStatistics ags = ManagerFactory.getInstance(this).statisticsManager.getByPlayerId(this, id);
                AggregatedStats statsToSend = new AggregatedStats();

                PlayerAggregatedStats as = new PlayerAggregatedStats();
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.gp), Long.toString(ags.getMatches()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.g), Long.toString(ags.getGoals()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.a), Long.toString(ags.getAssists()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.p), Long.toString(ags.getPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.pmp), Long.toString(ags.getPlusMinusPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.s), Long.toString(ags.getSaves()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.tp), Long.toString(ags.getTeamPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.w), Long.toString(ags.getWins()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.d), Long.toString(ags.getDraws()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.l), Long.toString(ags.getLosses()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ag), String.format("%.2f", ags.getAvgGoals()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ap), String.format("%.2f", ags.getAvgPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.apmp), String.format("%.2f", ags.getAvgPlusMinus()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.atp), String.format("%.2f", ags.getAvgTeamPoints()), false));
                statsToSend.addPlayerStats(as);

                Intent res = new Intent(sportContext + package_name + action);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_STATS, statsToSend);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_DELETE_COMPETITION: {
                Intent res = new Intent(action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                if (ManagerFactory.getInstance(this).competitionManager.delete(this, compId))
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_OK);
                else
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_FAILED);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Competition c = ManagerFactory.getInstance(this).competitionManager.getById(this, compId);
                c.setSportContext(sportContext);
                String json = CompetitionSerializer.getInstance(this).serialize(c).toJson();
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_NAME, c.getFilename());
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_TYPE, CrossPackageCommunicationConstants.EXTRA_EXPORT);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, json);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_IMPORT_INFO: {
                // TODO refactor
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition c = CompetitionSerializer.getInstance(this).deserialize(competition);

                ImportInfo competitionInfo = new CompetitionImportInfo(c.getName(), CompetitionTypes.teams());
                ArrayList<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
                ArrayList<PlayerImportInfo> playersInfo = new ArrayList<>();
                ArrayList<Conflict> playersModified = new ArrayList<>();

                List<ServerCommunicationItem> allSubItems = competition.getSubItems();
                List<ServerCommunicationItem> players = new ArrayList<>();
                List<ServerCommunicationItem> tournaments = new ArrayList<>();

                for (ServerCommunicationItem subItem : allSubItems) {
                    if (subItem.getType().equals("Player")) {
                        players.add(subItem);
                    } else if (subItem.getType().equals("Tournament")) {
                        tournaments.add(subItem);
                    }
                }

                /* TOURNAMENTS HANDLING */
                for (ServerCommunicationItem t : tournaments) {
                    List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

                    Tournament tournament = TournamentSerializer.getInstance(this).deserialize(t);

                    for (ServerCommunicationItem subItem : t.subItems) {
                        if (subItem.getType().equals("Player")) {
                            tournamentPlayers.add(subItem);
                        } else if (subItem.getType().equals("Team")) {
                            tournamentTeams.add(subItem);
                        } else if (subItem.getType().equals("ScoredMatch")) {
                            tournamentMatches.add(subItem);
                        }
                    }

                    tournamentsInfo.add(new TournamentImportInfo(tournament.getName(), tournamentPlayers.size(), tournamentTeams.size(), tournamentMatches.size()));
                }

                /* PLAYERS HANDLING */
                Map<Long, Player> allPlayers = ManagerFactory.getInstance(this).corePlayerManager.getAllPlayers(this);
                HashMap<String, Player> allPlayersMap = new HashMap<>();
                for (Player p : allPlayers.values()) {
                    allPlayersMap.put(p.getEmail(), p);
                }

                /* Import players */
                for (ServerCommunicationItem p : players) {
                    Player player = PlayerSerializer.getInstance(this).deserialize(p);
                    if (allPlayersMap.containsKey(player.getEmail())) {
                        Player matchedPlayer = allPlayersMap.get(player.getEmail());
                        if (!matchedPlayer.samePlayer(player)) {
                            playersModified.add(ConflictCreator.createConflict(matchedPlayer, player));
                            // TODO všechny attributes přeložit
                        }
                    } else {
                        playersInfo.add(new PlayerImportInfo(player.getName(), player.getEmail()));
                    }
                }

                Intent res = new Intent(package_name + action);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_TYPE, CrossPackageCommunicationConstants.EXTRA_IMPORT_INFO);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, json);
                res.putExtra(ImportActivity.COMPETITION, competitionInfo);
                res.putParcelableArrayListExtra(ImportActivity.TOURNAMENTS, tournamentsInfo);
                res.putParcelableArrayListExtra(ImportActivity.PLAYERS, playersInfo);
                res.putParcelableArrayListExtra(ImportActivity.CONFLICTS, playersModified);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_IMPORT_FILE_COMPETITION: {
                // TODO brutal refactor NEEDED
                // Begin transaction
                /*DatabaseFactory.getInstance().getDatabase(this).beginTransaction();
                try {
                */
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition c = CompetitionSerializer.getInstance(this).deserialize(competition);
                String competitionName = c.getName();
                c.setName(c.getName()+" "+ DateFormatter.getInstance().getDBDateTimeFormat().format(new Date()));

                HashMap<String, String> conflictSolutions = (HashMap<String, String>)intent.getExtras().getSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS);

                ManagerFactory.getInstance(this).competitionManager.insert(this, c);
                long competitionId = c.getId();

                List<ServerCommunicationItem> allSubItems = competition.getSubItems();
                List<ServerCommunicationItem> players = new ArrayList<>();
                List<ServerCommunicationItem> tournaments = new ArrayList<>();

                for (ServerCommunicationItem subItem : allSubItems) {
                    if (subItem.getType().equals("Player")) {
                        players.add(subItem);
                    } else if (subItem.getType().equals("Tournament")) {
                        tournaments.add(subItem);
                    }
                }

                /* PLAYERS HANDLING */
                Map<Long, Player> allPlayers = ManagerFactory.getInstance(this).corePlayerManager.getAllPlayers(this);
                HashMap<String, Player> allPlayersMap = new HashMap<>();
                for (Player p : allPlayers.values()) {
                    allPlayersMap.put(p.getEmail(), p);
                }

                /* Import players
                    - add if not exists
                    - add to competition
                    - create HashMap<uid, player> */
                HashMap<String, Player> importedPlayers = new HashMap<>();
                for (ServerCommunicationItem p : players) {
                    Log.d("IMPORT", "Player: "+p.syncData);
                    Player importedPlayer = PlayerSerializer.getInstance(this).deserialize(p);
                    long playerId;
                    if (allPlayersMap.containsKey(importedPlayer.getEmail())) {
                        Player matchedPlayer = allPlayersMap.get(importedPlayer.getEmail());
                        playerId = matchedPlayer.getId();
                        if (!matchedPlayer.samePlayer(importedPlayer)) {
                            if (conflictSolutions.containsKey(matchedPlayer.getEmail())) {
                                if (conflictSolutions.get(matchedPlayer.getEmail()).equals(Conflict.TAKE_FILE)) {
                                    ManagerFactory.getInstance(this).corePlayerManager.updatePlayer(this, importedPlayer);
                                    Log.d("IMPORT", "\tCONFLICT!");
                                    Log.d("IMPORT", "Player " + matchedPlayer.getEmail() + " will be replaced by file!");
                                }
                            }
                        }
                    } else {
                        ManagerFactory.getInstance(this).corePlayerManager.insertPlayer(this, importedPlayer);
                        playerId = importedPlayer.getId();
                        Log.d("IMPORT", "\tADDED "+playerId);
                    }
                    importedPlayer.setId(playerId);
                    importedPlayers.put(importedPlayer.getUid(), importedPlayer);

                    // Add player to competition.
                    ManagerFactory.getInstance(this).competitionManager.addPlayer(this, c, importedPlayer);
                }

                /* TOURNAMENTS HANDLING */
                for (ServerCommunicationItem t : tournaments) {
                    List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

                    Log.d("IMPORT", "Tournament: " + t.syncData);
                    Tournament imported = TournamentSerializer.getInstance(this).deserialize(t);
                    imported.setCompetitionId(competitionId);
                    ManagerFactory.getInstance(this).tournamentManager.insert(this, imported);
                    long tournamentId = imported.getId();

                    for (ServerCommunicationItem subItem : t.subItems) {
                        if (subItem.getType().equals("Player")) {
                            tournamentPlayers.add(subItem);
                        } else if (subItem.getType().equals("Team")) {
                            tournamentTeams.add(subItem);
                        } else if (subItem.getType().equals("ScoredMatch")) {
                            tournamentMatches.add(subItem);
                        }
                    }

                    for (ServerCommunicationItem p : tournamentPlayers) {
                        // Add player to tournament.
                        long playerId = importedPlayers.get(p.getUid()).getId();
                        ManagerFactory.getInstance(this).tournamentManager.addPlayer(this, playerId, tournamentId);
                    }

                    HashMap<String, Team> importedTeams = new HashMap<>();
                    for (ServerCommunicationItem team : tournamentTeams) {
                        // Add team to tournament.
                        Team importedTeam = TeamSerializer.getInstance(this).deserialize(team);
                        importedTeam.setTournamentId(tournamentId);
                        ManagerFactory.getInstance(this).teamManager.insert(this, importedTeam);
                        importedTeams.put(team.getUid(), importedTeam);

                        // Add players to team.
                        ArrayList<Player> teamPlayers = new ArrayList<>();
                        for (ServerCommunicationItem teamPlayer : team.subItems) {
                            if (teamPlayer.getType().equals("Player")) {
                                teamPlayers.add(importedPlayers.get(teamPlayer.getUid()));
                            }
                        }
                        ManagerFactory.getInstance(this).teamManager.updatePlayersInTeam(this, importedTeam.getId(), teamPlayers);
                    }

                    // Add stats
                    for (ServerCommunicationItem match : tournamentMatches) {
                        Match importedMatch = MatchSerializer.getInstance(this).deserialize(match);
                        importedMatch.setTournamentId(tournamentId);
                        boolean home = true;
                        for (ServerCommunicationItem matchTeam : match.subItems) {
                            if (home) {
//                                importedMatch.setHomeParticipantId(importedTeams.get(matchTeam.getUid()).getId());
                            } else {
//                                importedMatch.setAwayParticipantId(importedTeams.get(matchTeam.getUid()).getId());
                            }
                            home = false;
                        }
                        ManagerFactory.getInstance(this).matchManager.insert(this, importedMatch);
                        long matchId = importedMatch.getId();
                        if (importedMatch.isPlayed()) {
                            /* START match manager "begin match" */
                            Tournament tour = ManagerFactory.getInstance(this).tournamentManager.getById(this, importedMatch.getTournamentId());
                            List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(this, matchId);
                            for (Participant participant : participants) {
                                for (StatsEnum statEn : StatsEnum.values()) {
                                    if (statEn.isForPlayer()) continue;
                                    DStat statToAdd = new DStat(-1, -1, participant.getId(), statEn.toString(), importedMatch.getTournamentId(), tour.getCompetitionId(), String.valueOf(0));
                                    //DAOFactory.getInstance().statDAO.insert(this, statToAdd);
                                }
                            }
                            importedMatch.setPlayed(true);
                            ManagerFactory.getInstance(this).matchManager.update(this, importedMatch);

//                            DMatchStat matchStat = new DMatchStat(matchId, false, false);
//                            DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(this, matchStat);
                            /* END match manager "begin match" */

                            /* START statistics manager - set match score by match id */
                            int homeScore = Integer.parseInt(String.valueOf(match.syncData.get("score_home")));
                            int awayScore = Integer.parseInt(String.valueOf(match.syncData.get("score_away")));
                            boolean overtime = Boolean.parseBoolean(String.valueOf(match.syncData.get("overtime")));
                            boolean shootouts = Boolean.parseBoolean(String.valueOf(match.syncData.get("shootouts")));
//                            Match score = new Match(matchId, homeScore, awayScore, shootouts, overtime);
//                            ManagerFactory.getInstance(this).statisticsManager.setMatchScoreByMatchId(this, matchId, score);
                            /* END statistics manager - set match score by match id */

                            /* START statistics manager - update players in match */
                            /*ManagerFactory.getInstance(this).statisticsManager.updatePlayersInMatch(
                                    this, matchId, ParticipantType.home,
                                    getPlayerIds(match, "home", importedPlayers));
                            ManagerFactory.getInstance(this).statisticsManager.updatePlayersInMatch(
                                    this, matchId, ParticipantType.away,
                                    getPlayerIds(match, "away", importedPlayers));*/
                            /* END statistics manager - update players in match */

                            /* START statistics manager - update player stats in match */
                            //updatePlayersMatchStats(match, matchId, importedPlayers);
                            /* END statistics manager - update player stats in match */
                        }
                    }
                }
                /*
                } catch(Exception e) {} finally {
                    DatabaseFactory.getInstance().getDatabase(this).setTransactionSuccessful();
                }
                DatabaseFactory.getInstance().getDatabase(this).endTransaction();
                */
                break;
            }
        }
    }

    /*private ArrayList<Long> getPlayerIds(ServerCommunicationItem match, String role, HashMap<String, Player> players) {
        ArrayList<Long> playerIds = new ArrayList<>();
        int playersCnt = Integer.parseInt(match.syncData.get("players_"+role));
        for (int i=1; i<=playersCnt; i++) {
            String uid = match.syncData.get("player_"+role+"_"+i);
            playerIds.add(players.get(uid).getId());
        }
        return playerIds;
    }

    private void updatePlayersMatchStats(ServerCommunicationItem match, long matchId, HashMap<String, Player> players) {
        int playersCnt = Integer.parseInt(match.syncData.get("players_home"));
        for (int i=1; i<=playersCnt; i++) {
            String uid = match.syncData.get("player_home_"+i);
            long id = players.get(uid).getId();
            int goals = Integer.parseInt(match.syncData.get("stat_goals_"+uid));
            int assists = Integer.parseInt(match.syncData.get("stat_assists_"+uid));
            int plusMinusPoints = Integer.parseInt(match.syncData.get("stat_plus_minus_points_"+uid));
            int saves = Integer.parseInt(match.syncData.get("stat_saves_"+uid));

            MatchPlayerStatistic statistic = new MatchPlayerStatistic(id, "", goals, assists, plusMinusPoints, saves);
            ManagerFactory.getInstance(this).statisticsManager.updatePlayerStatsInMatch(this, statistic, matchId);
        }

        playersCnt = Integer.parseInt(match.syncData.get("players_away"));
        for (int i=1; i<=playersCnt; i++) {
            String uid = match.syncData.get("player_away_"+i);
            long id = players.get(uid).getId();
            int goals = Integer.parseInt(match.syncData.get("stat_goals_"+uid));
            int assists = Integer.parseInt(match.syncData.get("stat_assists_"+uid));
            int plusMinusPoints = Integer.parseInt(match.syncData.get("stat_plus_minus_points_"+uid));
            int saves = Integer.parseInt(match.syncData.get("stat_saves_"+uid));

            MatchPlayerStatistic statistic = new MatchPlayerStatistic(id, "", goals, assists, plusMinusPoints, saves);
            ManagerFactory.getInstance(this).statisticsManager.updatePlayerStatsInMatch(this, statistic, matchId);
        }
    }*/
}