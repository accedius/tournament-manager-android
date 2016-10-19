package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.squash.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Allows user to create or edit Competition
 * Created by Vaclav on 28. 3. 2016.
 */
public class NewSquashCompetitionFragment extends NewCompetitionFragment {
    @Override
    protected String getCompetitionKey() {
        return CompetitionService.EXTRA_COMPETITION;
    }

    @Override
    public void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, competitionId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_GET_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected boolean isTypeChoosable() {
        String sport_context = ((SquashPackage)getActivity().getApplication()).getSportContext();
        if (sport_context.equals("Volleyball"))
            return false;
        return true;
    }

    @Override
    protected CompetitionType defaultCompetitionType() {
        return CompetitionTypes.teams();
    }
}
