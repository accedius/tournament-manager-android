package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionListFragment extends AbstractListFragment {

    private static final String action = "org.cz.cvut.tournamentmanager.action";

    //private DataReceiver receiver = new DataReceiver();

    @Override
    protected void askForData() {
        Intent intent = CompetitionService.getStartIntent(action, getActivity());
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
        return new CompetitionAdapter();
    }

    @Override
    protected String getDataKey() {
        return CompetitionService.EXTRA_RESULT;
    }


}
