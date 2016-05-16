package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;

/**
 * Fragment for creating new match or editing one
 * Created by atgot_000 on 10. 4. 2016.
 */
public class NewHockeyMatchFragment extends NewMatchFragment {
    @Override
    protected void saveMatch(ScoredMatch match) {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_CREATE, getContext() );
        intent.setAction( MatchService.ACTION_CREATE );
        intent.putExtra(MatchService.EXTRA_MATCH, match);

        getContext().startService( intent );
    }

    @Override
    protected void updateMatch(ScoredMatch match) {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_UPDATE, getContext() );
        intent.setAction(MatchService.ACTION_UPDATE);
        intent.putExtra(MatchService.EXTRA_MATCH, match);

        getContext().startService( intent );
    }

    @Override
    protected String getMatchKey() {
        return MatchService.EXTRA_MATCH;
    }

    @Override
    protected String getTournamentParticipantsKey() {
        return MatchService.EXTRA_PART_LIST;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_FIND_BY_ID, getContext() );
        intent.putExtra( MatchService.EXTRA_ID, id );
        intent.putExtra( MatchService.EXTRA_TOUR_ID, tournamentId );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking( MatchService.ACTION_FIND_BY_ID );
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
