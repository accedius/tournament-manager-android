package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.hockey.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.stats.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
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
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE);
        String sport_context = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);
        ((HockeyPackage) getApplicationContext()).setSportContext(sport_context);

        switch (action) {
            case CrossPackageCommunicationConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                AggregatedStatistics ags = ManagerFactory.getInstance().statisticsManager.getByPlayerID(this, id);
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

                Intent res = new Intent(sport_context + package_name + action);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_STATS, statsToSend);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_DELETE_COMPETITION: {
                Intent res = new Intent(action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                if (ManagerFactory.getInstance().competitionManager.delete(this, compId))
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_OK);
                else
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_FAILED);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Competition c = ManagerFactory.getInstance().competitionManager.getById(this, compId);
                c.setSportContext(sport_context);
                String json = CompetitionSerializer.getInstance(this).serialize(c).toJson();
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_NAME, c.getFilename());
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_TYPE, CrossPackageCommunicationConstants.EXTRA_JSON);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, json);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_FILE_IMPORT_COMPETITION: {
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem item = gson.fromJson(json, ServerCommunicationItem.class);
                Competition c = CompetitionSerializer.getInstance(getApplicationContext()).deserialize(item);
                Long competitionId = ManagerFactory.getInstance().competitionManager.insert(getApplicationContext(), c);

                List<ServerCommunicationItem> allSubItems = item.getSubItems();
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
                ArrayList<Player> allPlayers = ManagerFactory.getInstance().packagePlayerManager.getAllPlayers(this);
                HashMap<String, Player> allPlayersMap = new HashMap<>();
                for (Player p : allPlayers) {
                    allPlayersMap.put(p.getEmail(), p);
                }

                /* Import players
                    - add if not exists
                    - add to competition */
                for (ServerCommunicationItem p : players) {
                    Log.d("IMPORT", "Player: "+p.syncData);
                    Player imported = PlayerSerializer.getInstance(getApplicationContext()).deserialize(p);
                    Long playerId;
                    if (allPlayersMap.containsKey(imported.getEmail())) {
                        playerId = allPlayersMap.get(imported.getEmail()).getId();
                        if (allPlayersMap.get(imported.getEmail()).samePlayer(imported)) {
                            Log.d("IMPORT", "\tSKIP");
                        } else {
                            Log.d("IMPORT", "\tCONFLICT!");
                        }
                    } else {
                        playerId = ManagerFactory.getInstance().packagePlayerManager.insertPlayer(this, imported);
                        Log.d("IMPORT", "\tADDED "+playerId);
                    }

                    // Add player to competition.
                    ManagerFactory.getInstance().packagePlayerManager.addPlayerToCompetition(this, playerId, competitionId);
                }

                /* TOURNAMENTS HANDLING */
                for (ServerCommunicationItem t : tournaments) {
                    Log.d("IMPORT", "Tournament: "+t.syncData);
                    Tournament imported = TournamentSerializer.getInstance(getApplicationContext()).deserialize(t);
                    imported.setCompetitionId(competitionId);
                    ManagerFactory.getInstance().tournamentManager.insert(this, imported);
                }
                break;
            }
        }
    }
}