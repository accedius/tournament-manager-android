package fit.cvut.org.cz.squash.presentation.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.loaders.CompetitionLoader;
import fit.cvut.org.cz.squash.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.entities.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;

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

                if (ManagerFactory.getInstance(this).competitionManager.delete(id))
                    result.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_OK);
                else
                    result.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_FAILED);

                sendBroadcast(result);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                SAggregatedStats stat = ManagerFactory.getInstance(this).statisticManager.getByPlayerId(id);
                AggregatedStats statsForExport = new AggregatedStats();

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

                Intent result = new Intent(sport_context + package_name + action);
                result.putExtra(CrossPackageCommunicationConstants.EXTRA_STATS, statsForExport);
                result.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                sendBroadcast(result);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).competitionManager.getById(compId);
                competition.setSportContext(sport_context);
                String json = CompetitionSerializer.getInstance(this).serialize(competition).toJson();

                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_NAME, competition.getFilename());
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_TYPE, CrossPackageCommunicationConstants.EXTRA_EXPORT);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, json);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_IMPORT_INFO: {
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);

                ArrayList<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
                ArrayList<PlayerImportInfo> playersInfo = new ArrayList<>();
                ArrayList<Conflict> playersModified = new ArrayList<>();
                ImportInfo competitionInfo = CompetitionLoader.getImportInfo(this, competition, tournamentsInfo, playersInfo, playersModified);

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
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Map<String, String> conflictSolutions = (HashMap<String, String>)intent.getExtras().getSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS);

                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition importedCompetition = CompetitionLoader.importCompetition(this, competition, conflictSolutions);
                break;
            }
        }
    }
}
