package fit.cvut.org.cz.squash.presentation.services;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.stats.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;

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
        String action = intent.getStringExtra(CrossPackageComunicationConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageComunicationConstants.EXTRA_PACKAGE);

        switch (action){
            case CrossPackageComunicationConstants.ACTION_DELETE_COMPETITION:{
                long id = intent.getLongExtra(CrossPackageComunicationConstants.EXTRA_ID, -1);
                Intent result = new Intent(action);

                if (ManagersFactory.getInstance().competitionManager.delete(this, id))
                    result.putExtra(CrossPackageComunicationConstants.EXTRA_OUTCOME, CrossPackageComunicationConstants.OUTCOME_OK);
                else
                    result.putExtra(CrossPackageComunicationConstants.EXTRA_OUTCOME, CrossPackageComunicationConstants.OUTCOME_FAILED);

                sendBroadcast(result);
                break;
            }
            case CrossPackageComunicationConstants.ACTION_GET_STATS:{
                long id = intent.getLongExtra(CrossPackageComunicationConstants.EXTRA_ID, -1);
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
                Intent result = new Intent(package_name+action);
                result.putExtra(CrossPackageComunicationConstants.EXTRA_STATS, statsForExport);
                sendBroadcast(result);
                break;
            }
        }
    }
}
