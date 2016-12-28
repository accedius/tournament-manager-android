package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.hockey.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Fragment for new competition or edit competition
 * Created by atgot_000 on 29. 3. 2016.
 */
public class NewHockeyCompetitionFragment extends NewCompetitionFragment {
    @Override
    protected String getCompetitionKey() {
        return CompetitionService.EXTRA_COMPETITION;
    }

    @Override
    public void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, competitionId);

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
    protected boolean isTypeChoosable() {
        return false;
    }

    @Override
    protected CompetitionType defaultCompetitionType() {
        return CompetitionTypes.teams();
    }
}
