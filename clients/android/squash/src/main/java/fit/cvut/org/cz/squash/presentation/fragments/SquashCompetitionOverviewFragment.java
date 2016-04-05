package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class SquashCompetitionOverviewFragment extends CompetitionOverviewFragment {
    @Override
    protected String getCompetitionKey() {
        return CompetitionService.EXTRA_COMPETITION;
    }

    @Override
    protected String getTournamentsSumKey() {
        return CompetitionService.EXTRA_TOURNAMENT_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return CompetitionService.EXTRA_PLAYERS_COUNT;
    }

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_GET_OVERVIEW, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, competitionID);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_GET_OVERVIEW);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_GET_OVERVIEW));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
