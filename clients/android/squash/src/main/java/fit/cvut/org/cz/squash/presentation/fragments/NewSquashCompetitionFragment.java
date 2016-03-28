package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;

import fit.cvut.org.cz.squash.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Created by Vaclav on 28. 3. 2016.
 */
public class NewSquashCompetitionFragment extends NewCompetitionFragment {

    @Override
    protected void saveCompetition(Competition c) {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_CREATE, getContext());
        intent.putExtra(CompetitionService.EXTRA_COMPETITION, c);

        getContext().startService(intent);
    }

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, competitionId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_GET_BY_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {

        Competition competition = intent.getParcelableExtra(CompetitionService.EXTRA_COMPETITION);
        bindCompetitionOnView(competition);

    }

    @Override
    protected void registerReceivers() {
        getContext().registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        getContext().unregisterReceiver(receiver);
    }
}
