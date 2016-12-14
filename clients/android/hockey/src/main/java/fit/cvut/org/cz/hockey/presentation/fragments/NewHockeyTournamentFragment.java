package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * Fragment for creating new tournament or editing one
 * Created by atgot_000 on 5. 4. 2016.
 */
public class NewHockeyTournamentFragment extends NewTournamentFragment {
    @Override
    protected void saveTournament(Tournament t) {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_CREATE, getContext());
        intent.putExtra(TournamentService.EXTRA_TOURNAMENT, t);

        getContext().startService(intent);
    }

    @Override
    protected void updateTournament(Tournament t) {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_UPDATE, getContext());
        intent.putExtra(TournamentService.EXTRA_TOURNAMENT, t);

        getContext().startService(intent);
    }

    @Override
    protected String getTournamentKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(TournamentService.EXTRA_ID, tournamentId);

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
