package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Date;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;

/**
 * Fragment is used in CreateMatchActivity to show match creation panel
 */
public class NewBowlingMatchFragment extends CustomNewMatchFragment {
    @Override
    protected String getMatchKey() {
        return ExtraConstants.EXTRA_MATCH;
    }

    @Override
    protected String getTournamentParticipantsKey() {
        return ExtraConstants.EXTRA_PARTICIPANTS;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, id);
        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, tournamentId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
