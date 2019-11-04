package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Fragment is used in
 */
public class BowlingCompetitionOverviewFragment extends CompetitionOverviewFragment {
    @Override
    public void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, competitionId);
        Context context = getContext();
        context.startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }

    @Override
    protected String getCompetitionKey() {
        return ExtraConstants.EXTRA_COMPETITION;
    }

    @Override
    protected String getTournamentsSumKey() {
        return ExtraConstants.EXTRA_TOURNAMENTS_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return ExtraConstants.EXTRA_PLAYERS_COUNT;
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = super.injectView(inflater, container);
        v.findViewById(R.id.comp_type_label).setVisibility(View.VISIBLE);
        v.findViewById(R.id.comp_type).setVisibility(View.VISIBLE);
        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        super.bindDataOnView(intent);
        CompetitionType competitionType;
        int typeId = competition.getTypeId();
        if(typeId != CompetitionTypes.type_none) {
            try {
                competitionType = CompetitionTypes.getTypeByTypeId(typeId);
            } catch (Exception e) {
                competitionType = CompetitionTypes.none();
            }
            type.setText(competitionType.value);
        } else {
            getView().findViewById(R.id.comp_type_label).setVisibility(View.GONE);
            getView().findViewById(R.id.comp_type).setVisibility(View.GONE);
        }
    }
}
