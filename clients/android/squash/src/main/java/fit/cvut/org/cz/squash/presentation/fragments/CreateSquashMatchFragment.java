package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;

/**
 * Created by Vaclav on 20. 4. 2016.
 */
public class CreateSquashMatchFragment extends NewMatchFragment {


    @Override
    protected void saveMatch(ScoredMatch match) {
        Intent i = MatchService.newStartIntent(MatchService.ACTION_CREATE_MATCH, getContext());
        i.setAction(MatchService.ACTION_CREATE_MATCH);
        match.setTournamentId(tournamentId);
        match.setPlayed(false);
        i.putExtra(MatchService.EXTRA_MATCH, match);
        getContext().startService(i);
    }

    @Override
    protected void updateMatch(ScoredMatch match) {
        Intent i = MatchService.newStartIntent(MatchService.ACTION_UPDATE_MATCH, getContext());
        i.setAction(MatchService.ACTION_UPDATE_MATCH);
        i.putExtra(MatchService.EXTRA_MATCH, match);
        getContext().startService(i);
    }

    @Override
    protected String getMatchKey() {
        return MatchService.EXTRA_MATCH;
    }

    @Override
    protected String getTournamentParticipantsKey() {
        return MatchService.EXTRA_PARTICIPANTS;
    }

    @Override
    protected void askForData() {

        if (tournamentId != -1){
            Intent intent = null;
            if (id == -1) intent = MatchService.newStartIntent(MatchService.ACTION_GET_PARTICIPANTS_FOR_MATCH, getContext());
            else {
                intent = MatchService.newStartIntent(MatchService.ACTION_GET_MATCH_BY_ID, getContext());
                intent.putExtra(MatchService.EXTRA_MATCH_ID, id);
            }
            intent.putExtra(MatchService.EXTRA_ID, tournamentId);
            getContext().startService(intent);
        }

    }

    @Override
    protected boolean isDataSourceWorking() {
        if (id != -1)
            return MatchService.isWorking(MatchService.ACTION_GET_MATCH_BY_ID);
        else return MatchService.isWorking(MatchService.ACTION_GET_PARTICIPANTS_FOR_MATCH);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(MatchService.ACTION_GET_MATCH_BY_ID);
        filter.addAction(MatchService.ACTION_GET_PARTICIPANTS_FOR_MATCH);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
