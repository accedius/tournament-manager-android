package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

public class BowlingCompetitionOverviewFragment extends CompetitionOverviewFragment {
    @Override
    public void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, competitionId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected String getCompetitionKey() {
        return ExtraConstants.EXTRA_COMPETITION;
    }

    @Override
    protected String getTournamentsSumKey() {
        return ExtraConstants.EXTRA_TOURNAMENTS_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return ExtraConstants.EXTRA_PLAYERS_COUNT;
    }
}
