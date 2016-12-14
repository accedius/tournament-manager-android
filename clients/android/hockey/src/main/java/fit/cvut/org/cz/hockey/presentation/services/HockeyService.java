package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.loaders.CompetitionLoader;
import fit.cvut.org.cz.hockey.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStatsRecord;
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
                AggregatedStatistics ags = ManagerFactory.getInstance(this).statisticsManager.getByPlayerId(id);

                ArrayList<PlayerAggregatedStats> statsToSend = new ArrayList<>();
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
                statsToSend.add(as);

                Intent res = new Intent(sportContext + package_name + action);
                res.putParcelableArrayListExtra(CrossPackageCommunicationConstants.EXTRA_STATS, statsToSend);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_DELETE_COMPETITION: {
                Intent res = new Intent(action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                if (ManagerFactory.getInstance(this).competitionManager.delete(compId))
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_OK);
                else
                    res.putExtra(CrossPackageCommunicationConstants.EXTRA_OUTCOME, CrossPackageCommunicationConstants.OUTCOME_FAILED);
                sendBroadcast(res);
                break;
            }
            case CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageCommunicationConstants.EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).competitionManager.getById(compId);
                competition.setSportContext(sportContext);
                String json = CompetitionSerializer.getInstance(this).serialize(competition).toJson();

                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
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
                // TODO Begin transaction ?
                /*DatabaseFactory.getInstance().getDatabase(this).beginTransaction();
                try {
                */
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                Map<String, String> conflictSolutions = (HashMap<String, String>)intent.getExtras().getSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS);

                Gson gson = new GsonBuilder().serializeNulls().create();
                ServerCommunicationItem competition = gson.fromJson(json, ServerCommunicationItem.class);
                Competition importedCompetition = CompetitionLoader.importCompetition(this, competition, conflictSolutions);
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
}