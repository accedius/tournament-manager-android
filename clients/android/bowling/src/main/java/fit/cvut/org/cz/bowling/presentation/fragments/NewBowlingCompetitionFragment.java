package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.bowling.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Fragment is used in CreateCompetitionActivity to show creation panel view
 */
public class NewBowlingCompetitionFragment extends NewCompetitionFragment {
    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v =  super.injectView(inflater, container);
        adapter.clear();
        adapter.addAll(CompetitionTypes.competitionTypes(getResources()));
        type.setAdapter(adapter);
        return v;
    }

    @Override
    protected void bindCompetitionOnView(final Competition c) {
        super.bindCompetitionOnView(c);
        int typeId = c.getTypeId();
        int index = CompetitionTypes.getIndexByTypeId(typeId);
        type.setSelection(index);
    }

    @Override
    protected String getCompetitionKey() {
        return ExtraConstants.EXTRA_COMPETITION;
    }

    @Override
    public void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, competitionId);

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
        return true;
    }

    @Override
    protected CompetitionType defaultCompetitionType() {
        return CompetitionTypes.none();
    }
}
