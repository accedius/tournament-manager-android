package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Fragment for tournament overview
 * Created by atgot_000 on 8. 4. 2016.
 */
public class HockeyTournamentOverviewFragment extends TournamentOverviewFragment {
    @Override
    protected String getTournamentKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    protected String getMatchesSumKey() {
        return TournamentService.EXTRA_MATCHES_SUM;
    }

    @Override
    protected String getPlayersSumKey() {
        return TournamentService.EXTRA_PLAYER_SUM;
    }

    @Override
    protected String getTeamsSumKey() {
        return TournamentService.EXTRA_TEAMS_SUM;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(TournamentService.EXTRA_ID, tournamentID);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
