package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;

import fit.cvut.org.cz.hockey.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class NewHockeyCompetitionFragment extends NewCompetitionFragment {
    @Override
    protected void saveCompetition(Competition c) {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_CREATE, getContext());
        intent.putExtra(CompetitionService.EXTRA_COMPETITION, c);

        getContext().startService(intent);
    }

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, competitionId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Competition c = intent.getParcelableExtra(CompetitionService.EXTRA_COMPETITION);
        bindCompetitionOnView( c );
    }

    @Override
    protected void registerReceivers() {
        getContext().registerReceiver(receiver, new IntentFilter(CompetitionService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        getContext().unregisterReceiver(receiver);
    }

}
