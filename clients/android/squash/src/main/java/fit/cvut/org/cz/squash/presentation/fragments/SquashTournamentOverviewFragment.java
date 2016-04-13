package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class SquashTournamentOverviewFragment extends TournamentOverviewFragment {
    @Override
    protected String getTournamentKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    protected String getMatchesSumKey() {
        return TournamentService.EXTRA_MATCH_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return TournamentService.EXTRA_PLAYER_COUNT;
    }

    @Override
    protected String getTeamsSumKey() {
        return TournamentService.EXTRA_TEAM_COUNT;
    }

    @Override
    protected void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_OVERVIEW, getContext());
        intent.putExtra(TournamentService.EXTRA_ID, tournamentID);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_OVERVIEW);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_GET_OVERVIEW));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
