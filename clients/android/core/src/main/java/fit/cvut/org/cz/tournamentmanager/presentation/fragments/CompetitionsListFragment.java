package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionsListFragment extends AbstractListFragment {

    private String action = "org.cz.cvut.tournamentmanager.action";

    private String package_name;
    private String activity_detail_competition;

    //private DataReceiver receiver = new DataReceiver();

    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return this.action;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        package_name = getArguments().getString("package_name");
        activity_detail_competition = getArguments().getString("activity_detail_competition");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.getStartIntent(this.action, this.package_name, getActivity());
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(action);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(action));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new CompetitionAdapter(package_name, activity_detail_competition, getActivity());
    }

    @Override
    protected String getDataKey() {
        return CompetitionService.EXTRA_RESULT;
    }


}
