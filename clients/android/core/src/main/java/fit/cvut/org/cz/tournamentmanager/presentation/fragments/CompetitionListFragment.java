package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;

import java.util.AbstractList;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionListFragment extends AbstractListFragment {

    private static final String action = "org.cz.cvut.tournamentmanager.action";

    //private DataReceiver receiver = new DataReceiver();

    @Override
    protected void registerReceivers() {

        getActivity().registerReceiver(receiver, new IntentFilter(action));

    }

    @Override
    protected void unregisterReceivers() {

        getActivity().unregisterReceiver(receiver);

    }

    @Override
    protected void getData() {

        Intent intent = CompetitionService.getStartIntent(action, getActivity());
        getActivity().startService(intent);

    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return null;
    }
}
