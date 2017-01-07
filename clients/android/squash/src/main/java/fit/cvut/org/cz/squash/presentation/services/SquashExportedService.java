package fit.cvut.org.cz.squash.presentation.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.loaders.CompetitionLoader;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;

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
        String action = intent.getStringExtra(CrossPackageConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE);
        String sport_context = intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        ((SquashPackage) getApplicationContext()).setSportContext(sport_context);

        switch (action){
            case CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS: {
                List<Competition> competitions = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getAll();
                for (Competition competition : competitions) {
                    competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);
                }

                Intent res = new Intent(package_name + action);
                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.TYPE_COMPETITION);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_COMPETITION, new ArrayList<>(competitions));
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER: {
                Intent res = new Intent(package_name + action);
                long playerId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                ArrayList<Competition> competitions = new ArrayList<>();
                List<Competition> allCompetitions = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getAll();

                boolean containsPlayer;
                for (Competition competition : allCompetitions) {
                    containsPlayer = false;
                    List<Player> players = ((ICompetitionManager) ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayers(competition.getId());
                    for (Player player : players) {
                        if (player.getId() == playerId) {
                            containsPlayer = true;
                            break;
                        }
                    }

                    if (containsPlayer)
                        competitions.add(competition);
                }

                for (Competition competition : competitions) {
                    competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);
                }

                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_COMPETITION, new ArrayList<>(competitions));
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_DELETE_COMPETITION: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                res.putExtra(CrossPackageConstants.EXTRA_RESULT, ManagerFactory.getInstance(this).getEntityManager(Competition.class).delete(compId));

                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.TYPE_DELETE);
                res.putExtra(CrossPackageConstants.EXTRA_POSITION, intent.getIntExtra(CrossPackageConstants.EXTRA_POSITION, -1));
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                SAggregatedStats stat = ((IStatisticManager)ManagerFactory.getInstance(this).getEntityManager(SAggregatedStats.class)).getByPlayerId(id);

                ArrayList<PlayerAggregatedStats> statsForExport = new ArrayList<>();
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
                statsForExport.add(exportStat);

                Intent result = new Intent(sport_context + package_name + action);
                result.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_STATS, statsForExport);
                result.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                sendBroadcast(result);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(compId);
                competition.setSportContext(sport_context);
                String json = CompetitionSerializer.getInstance(this).serialize(competition).toJson();

                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageConstants.EXTRA_NAME, competition.getFilename());
                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.EXTRA_EXPORT);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, json);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITION_IMPORT_INFO: {
                String json = intent.getStringExtra(CrossPackageConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);

                ArrayList<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
                ArrayList<PlayerImportInfo> playersInfo = new ArrayList<>();
                ArrayList<Conflict> playersModified = new ArrayList<>();
                CompetitionImportInfo competitionInfo = CompetitionLoader.getImportInfo(this, getResources(), competition, tournamentsInfo, playersInfo, playersModified);

                Intent res = new Intent(package_name + action);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.EXTRA_IMPORT_INFO);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, json);
                res.putExtra(ExtraConstants.EXTRA_COMPETITION, competitionInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENTS, tournamentsInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, playersInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_CONFLICTS, playersModified);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_IMPORT_FILE_COMPETITION: {
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageConstants.EXTRA_JSON);
                Map<String, String> conflictSolutions = (HashMap<String, String>)intent.getExtras().getSerializable(CrossPackageConstants.EXTRA_CONFLICTS);

                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition importedCompetition = CompetitionLoader.importCompetition(this, competition, conflictSolutions);
                break;
            }
        }
    }
}
