package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Allows usser to display tournament overview
 * Created by Vaclav on 13. 4. 2016.
 */
public class SquashTournamentOverviewFragment extends TournamentOverviewFragment {
    public static TournamentOverviewFragment newInstance(long tournamentId, CompetitionType type){
        TournamentOverviewFragment fragment = new SquashTournamentOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
        args.putInt(ExtraConstants.EXTRA_TYPE, type.id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTournamentKey() {
        return ExtraConstants.EXTRA_TOURNAMENT;
    }

    @Override
    protected String getMatchesSumKey() {
        return ExtraConstants.EXTRA_MATCHES_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return ExtraConstants.EXTRA_PLAYERS_COUNT;
    }

    @Override
    protected String getTeamsSumKey() {
        return ExtraConstants.EXTRA_TEAMS_COUNT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_OVERVIEW, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, tournamentId);
        intent.putExtra(ExtraConstants.EXTRA_TYPE, getArguments().getInt(ExtraConstants.EXTRA_TYPE));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_OVERVIEW);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_GET_OVERVIEW));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        CompetitionType type = CompetitionTypes.competitionTypes()[intent.getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
        super.bindDataOnView(intent);
        if (type.equals(CompetitionTypes.individuals())) {
            teamSum.setVisibility(View.GONE);
            teamsLabel.setVisibility(View.GONE);
        }
    }
}
