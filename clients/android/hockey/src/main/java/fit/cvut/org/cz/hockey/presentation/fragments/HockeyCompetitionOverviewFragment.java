package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.ShowCompetitionActivity;
import fit.cvut.org.cz.hockey.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class HockeyCompetitionOverviewFragment extends AbstractDataFragment {

    private TextView name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_competition_overview, container, false);

        name = (TextView) v.findViewById(R.id.comp_name);
        name.append( "cekam na append: ");

        return v;
    }

    @Override
    protected void askForData() {

        Intent intent = CompetitionService.newStartIntent(CompetitionService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(CompetitionService.EXTRA_ID, ((ShowCompetitionActivity) getActivity()).getCompetitionID());

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(CompetitionService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Competition competition = intent.getParcelableExtra( CompetitionService.EXTRA_COMPETITION );

        name.append(competition.getName());
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter(CompetitionService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        getContext().unregisterReceiver(receiver);
    }
    //TODO
}
