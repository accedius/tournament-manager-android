package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Allows usser to display tournament overview
 * Created by Vaclav on 13. 4. 2016.
 */
public class SquashTournamentOverviewFragment extends TournamentOverviewFragment {
    private static final String ARG_TYPE = "arg_type";

    public static TournamentOverviewFragment newInstance(long tournamentId, CompetitionType type){
        TournamentOverviewFragment fragment = new SquashTournamentOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(TOUR_KEY, tournamentId);
        args.putInt(ARG_TYPE, type.id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTournamentKey() {
        return TournamentService.EXTRA_TOURNAMENT;
    }

    @Override
    protected String getMatchesSumKey() {
        return TournamentService.EXTRA_MATCH_COUNT;
    }

    @Override
    protected String getPlayersSumKey() {
        return TournamentService.EXTRA_PLAYER_COUNT;
    }

    @Override
    protected String getTeamsSumKey() {
        return TournamentService.EXTRA_TEAM_COUNT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_OVERVIEW, getContext());
        intent.putExtra(TournamentService.EXTRA_ID, tournamentID);
        intent.putExtra(TournamentService.EXTRA_TYPE, getArguments().getInt(ARG_TYPE));
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
        CompetitionType type = CompetitionTypes.competitionTypes()[intent.getIntExtra(TournamentService.EXTRA_TYPE, 0)];
        super.bindDataOnView(intent);
        if (type.equals(CompetitionTypes.individuals())) {
            teamSum.setVisibility(View.GONE);
            teamsLabel.setVisibility(View.GONE);
        }
    }
}
