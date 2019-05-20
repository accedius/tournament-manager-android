package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

public class BowlingTournamentOverviewFragment extends TournamentOverviewFragment {
    @Override
    protected String getTournamentKey() {
        return ExtraConstants.EXTRA_TOURNAMENT;
    }

    @Override
    protected String getMatchesSumKey() {
        return ExtraConstants.EXTRA_MATCHES_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return ExtraConstants.EXTRA_PLAYERS_COUNT;
    }

    @Override
    protected String getTeamsSumKey() {
        return ExtraConstants.EXTRA_TEAMS_COUNT;
    }

    @Override
    public void askForData() {
        Context context = getContext();
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_FIND_BY_ID, context);
        intent.putExtra(ExtraConstants.EXTRA_ID, tournamentId);
        TournamentService.enqueueWork(getContext(), intent, TournamentService.class);
        //getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter(TournamentService.ACTION_FIND_BY_ID);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void unregisterReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }
}

