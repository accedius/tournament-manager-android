package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;

/**Allows user to edit or create match
 * Created by Vaclav on 20. 4. 2016.
 */
public class CreateSquashMatchFragment extends NewMatchFragment {
    @Override
    protected String getMatchKey() {
        return ExtraConstants.EXTRA_MATCH;
    }

    @Override
    protected String getTournamentParticipantsKey() {
        return ExtraConstants.EXTRA_PART_LIST;
    }

    @Override
    public void askForData() {
        if (tournamentId != -1) {
            Intent intent = null;
            if (id == -1)
                intent = MatchService.newStartIntent(MatchService.ACTION_GET_PARTICIPANTS_FOR_MATCH, getContext());
            else {
                intent = MatchService.newStartIntent(MatchService.ACTION_GET_MATCH_BY_ID, getContext());
                intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, id);
            }
            intent.putExtra(ExtraConstants.EXTRA_ID, tournamentId);
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
