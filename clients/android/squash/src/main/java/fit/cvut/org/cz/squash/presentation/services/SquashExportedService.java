package fit.cvut.org.cz.squash.presentation.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.squash.business.serialization.MatchSerializer;
import fit.cvut.org.cz.squash.business.serialization.TeamSerializer;
import fit.cvut.org.cz.squash.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;

/**
 * Allows Core to delete Competition and get stats for player
 * Created by Vaclav on 6. 5. 2016.
 */
public class SquashExportedService extends IntentService {
    public SquashExportedService() {
        super("Squash exported service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE);
        String sport_context = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);
        ((SquashPackage) getApplicationContext()).setSportContext(sport_context);

        switch (action){
            case CrossPackageCommunicationConstants.ACTION_DELETE_COMPETITION: {
                long id = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Intent result = new Intent(action);

                if (ManagersFactory.getInstance().competitionManager.delete(this, id))
                    result.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_OK);
                else
                    result.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_FAILED);

                sendBroadcast(result);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                ArrayList<SAggregatedStats> stats = ManagersFactory.getInstance().statsManager.getAggregatedStatsByPlayerId(this, id);
                AggregatedStats statsForExport = new AggregatedStats();

                for (SAggregatedStats stat : stats) {
                    PlayerAggregatedStats exportStat = new PlayerAggregatedStats();
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.wins), Integer.toString(stat.won), true));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.loses), Integer.toString(stat.lost), true));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.draws), Integer.toString(stat.draws), true));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.winsPer), String.format("%.2f", stat.matchWinRate), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.setsWon), Integer.toString(stat.setsWon), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.setsLost), Integer.toString(stat.setsLost), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.setsWonAvg), String.format("%.2f", stat.setsWonAvg), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.setsLostAvg), String.format("%.2f", stat.setsLostAvg), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.setsPer), String.format("%.2f", stat.setsWinRate), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.ballsWon), String.format("%d", stat.ballsWon), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.ballsLost), String.format("%d", stat.ballsLost), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.ballsWonAvg), String.format("%.2f", stat.ballsWonAvg), false));
                    exportStat.addRecord(new PlayerAggregatedStatsRecord(getResources().getString(R.string.ballsLostAvg), String.format("%.2f", stat.ballsLostAvg), false));

                    statsForExport.addPlayerStats(exportStat);
                }
                Intent result = new Intent(sport_context + package_name + action);
                result.putExtra(CrossPackageCommunicationConstants.EXTRA_STATS, statsForExport);
                result.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                sendBroadcast(result);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Competition c = ManagersFactory.getInstance().competitionManager.getById(this, compId);
                c.setSportContext(sport_context);
                String json = CompetitionSerializer.getInstance(this).serialize(c).toJson();
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
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

                ImportInfo competitionInfo = new CompetitionImportInfo(c.getName(), c.getType());
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
                ArrayList<Player> allPlayers = ManagersFactory.getInstance().playerManager.getAllPlayers(this);
                HashMap<String, Player> allPlayersMap = new HashMap<>();
                for (Player p : allPlayers) {
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
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
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

                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition c = CompetitionSerializer.getInstance(this).deserialize(competition);
                c.setName(c.getName() + " " + DateFormatter.getInstance().getDBDateTimeFormat().format(new Date()));
                long competitionId = ManagersFactory.getInstance().competitionManager.insert(this, c);

                HashMap<String, String> conflictSolutions = (HashMap<String, String>)intent.getExtras().getSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS);

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
                ArrayList<Player> allPlayers = ManagersFactory.getInstance().playerManager.getAllPlayers(this);
                HashMap<String, Player> allPlayersMap = new HashMap<>();
                for (Player p : allPlayers) {
                    allPlayersMap.put(p.getEmail(), p);
                }

                /* Import players
                    - add if not exists
                    - add to competition
                    - create HashMap<uid, player> */
                HashMap<String, Player> importedPlayers = new HashMap<>();
                for (ServerCommunicationItem p : players) {
                    Log.d("IMPORT", "Player: " + p.syncData);
                    Player importedPlayer = PlayerSerializer.getInstance(this).deserialize(p);
                    long playerId;
                    if (allPlayersMap.containsKey(importedPlayer.getEmail())) {
                        Player matchedPlayer = allPlayersMap.get(importedPlayer.getEmail());
                        playerId = matchedPlayer.getId();
                        if (!matchedPlayer.samePlayer(importedPlayer)) {
                            if (conflictSolutions.containsKey(matchedPlayer.getEmail())) {
                                if (conflictSolutions.get(matchedPlayer.getEmail()).equals(Conflict.TAKE_FILE)) {
                                    ManagersFactory.getInstance().playerManager.updatePlayer(this, importedPlayer);
                                    Log.d("IMPORT", "\tCONFLICT!");
                                    Log.d("IMPORT", "Player " + matchedPlayer.getEmail() + " will be replaced by file!");
                                }
                            }
                        }
                    } else {
                        playerId = ManagersFactory.getInstance().playerManager.insertPlayer(this, importedPlayer);
                        Log.d("IMPORT", "\tADDED " + playerId);
                    }
                    importedPlayer.setId(playerId);
                    importedPlayers.put(importedPlayer.getUid(), importedPlayer);

                    // Add player to competition.
                    ManagersFactory.getInstance().playerManager.addPlayerToCompetition(this, playerId, competitionId);
                }

                /* TOURNAMENTS HANDLING */
                for (ServerCommunicationItem t : tournaments) {
                    List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
                    List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

                    Log.d("IMPORT", "Tournament: " + t.syncData);
                    Tournament imported = TournamentSerializer.getInstance(this).deserialize(t);
                    imported.setCompetitionId(competitionId);
                    long tournamentId = ManagersFactory.getInstance().tournamentManager.insert(this, imported);

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
                        ManagersFactory.getInstance().playerManager.addPlayerToTournament(this, playerId, tournamentId);
                    }

                    HashMap<String, Team> importedTeams = new HashMap<>();
                    for (ServerCommunicationItem team : tournamentTeams) {
                        // Add team to tournament.
                        Team importedTeam = TeamSerializer.getInstance(this).deserialize(team);
                        importedTeam.setTournamentId(tournamentId);
                        long teamId = ManagersFactory.getInstance().teamsManager.insert(this, importedTeam);
                        importedTeam.setId(teamId);
                        importedTeams.put(team.getUid(), importedTeam);

                        // Add players to team.
                        ArrayList<Player> teamPlayers = new ArrayList<>();
                        for (ServerCommunicationItem teamPlayer : team.subItems) {
                            if (teamPlayer.getType().equals("Player")) {
                                teamPlayers.add(importedPlayers.get(teamPlayer.getUid()));
                            }
                        }
                        ManagersFactory.getInstance().playerManager.updatePlayersInTeam(this, teamId, teamPlayers);
                    }

                    // Add match
                    for (ServerCommunicationItem match : tournamentMatches) {
                        ScoredMatch importedMatch = MatchSerializer.getInstance(this).deserialize(match);
                        importedMatch.setTournamentId(tournamentId);
                        boolean home = true;
                        long homeParticipantId = -1, awayParticipantId = -1;
                        for (ServerCommunicationItem matchParticipant : match.subItems) {
                            if (CompetitionTypes.teams().equals(c.getType())) {
                                if (home) {
                                    homeParticipantId = importedTeams.get(matchParticipant.getUid()).getId();
                                } else {
                                    awayParticipantId = importedTeams.get(matchParticipant.getUid()).getId();
                                }
                            } else {
                                if (home) {
                                    homeParticipantId = importedPlayers.get(matchParticipant.getUid()).getId();
                                } else {
                                    awayParticipantId = importedPlayers.get(matchParticipant.getUid()).getId();
                                }
                            }
                            home = false;
                        }
                        importedMatch.setHomeParticipantId(homeParticipantId);
                        importedMatch.setAwayParticipantId(awayParticipantId);

                        /* START Match Manager - insert */
                        long matchId = DAOFactory.getInstance().matchDAO.insert(this, ScoredMatch.convertToDMatch(importedMatch));
                        DParticipant homeParticipant = null;
                        DParticipant awayParticipant = null;
                        if (c.getType().equals(CompetitionTypes.individuals())) {
                            homeParticipant = new DParticipant(-1, -1, matchId, "home");
                            awayParticipant = new DParticipant(-1, -1, matchId, "away");
                            homeParticipantId = DAOFactory.getInstance().participantDAO.insert(this, homeParticipant);
                            awayParticipantId = DAOFactory.getInstance().participantDAO.insert(this, awayParticipant);
                            //for individuals we have to insert match participation as well else we could not link match to player
                            DAOFactory.getInstance().statDAO.insert(this, new DStat(-1, competitionId, t.getId(), importedMatch.getHomeParticipantId(), homeParticipantId, -1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
                            DAOFactory.getInstance().statDAO.insert(this, new DStat(-1, competitionId, t.getId(), importedMatch.getAwayParticipantId(), awayParticipantId, -1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
                        } else {
                            homeParticipant = new DParticipant(-1, importedMatch.getHomeParticipantId(), matchId, "home");
                            awayParticipant = new DParticipant(-1, importedMatch.getAwayParticipantId(), matchId, "away");
                            homeParticipantId = DAOFactory.getInstance().participantDAO.insert(this, homeParticipant);
                            awayParticipantId = DAOFactory.getInstance().participantDAO.insert(this, awayParticipant);
                        }
                        /* END Match Manager - insert */

                        // Add teams rosters to match
                        if (CompetitionTypes.teams().equals(c.getType())) {
                            ArrayList<Player> matchPlayers = getPlayers(match, "home", importedPlayers);
                            ManagersFactory.getInstance().participantManager.updatePlayersForMatch(this, matchId, "home", matchPlayers);
                            matchPlayers = getPlayers(match, "away", importedPlayers);
                            ManagersFactory.getInstance().participantManager.updatePlayersForMatch(this, matchId, "away", matchPlayers);
                        }

                        // Add sets
                        /* START Stats Manager - updateStatsForMatch */
                        saveStatsForMatch(match, competitionId, tournamentId, homeParticipantId, awayParticipantId);
                        /* END Stats Manager - updateStatsForMatch */
                    }
                }
                break;
            }
        }
    }

    void saveStatsForMatch(ServerCommunicationItem match, long competitionId, long tournamentId, long homeParticipantId, long awayParticipantId) {
        int setsWon = 0;
        int setsCnt = Integer.parseInt(match.syncData.get("sets"));
        for (int i = 1; i <= setsCnt; i++) {
            SetRowItem item = new SetRowItem();
            item.setHomeScore(Integer.parseInt(match.syncData.get("set_home_"+i)));
            item.setAwayScore(Integer.parseInt(match.syncData.get("set_away_"+i)));

            if (item.getHomeScore() > item.getAwayScore())
                item.setWinner(1);
            else if (item.getHomeScore() < item.getAwayScore())
                item.setWinner(-1);

            DAOFactory.getInstance().statDAO.insert(this, new fit.cvut.org.cz.squash.data.entities.DStat(-1, competitionId, tournamentId, -1, homeParticipantId,
                    item.getWinner(), item.getAwayScore(), item.getHomeScore(), StatsEnum.SET));
            DAOFactory.getInstance().statDAO.insert(this, new fit.cvut.org.cz.squash.data.entities.DStat(-1, competitionId, tournamentId, -1, awayParticipantId,
                    item.getWinner() * -1, item.getHomeScore(), item.getAwayScore(), StatsEnum.SET));
            setsWon += item.getWinner();
        }
        int result = 0;
        if (setsWon > 0) result = 1;
        if (setsWon < 0) result = -1;
        DAOFactory.getInstance().statDAO.insert(this, new DStat(-1, competitionId, tournamentId, -1, homeParticipantId,
                result, -1, -1, StatsEnum.MATCH));
        DAOFactory.getInstance().statDAO.insert(this, new DStat(-1, competitionId, tournamentId, -1, awayParticipantId,
                result * -1, -1, -1, StatsEnum.MATCH));
    }

    ArrayList<Player> getPlayers(ServerCommunicationItem match, String role, HashMap<String, Player> playersMap) {
        ArrayList<Player> players = new ArrayList<>();
        int playersCnt = Integer.parseInt(match.syncData.get("players_"+role));
        for (int i=1; i<=playersCnt; i++) {
            String uid = match.syncData.get("player_"+role+"_"+i);
            players.add(playersMap.get(uid));
        }
        return players;
    }
}
