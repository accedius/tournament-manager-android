package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class HockeyTournamentsListFragment extends AbstractListFragment<Tournament> {

    private long competitionID;
    private static String ARG_ID = "competition_id";

    public static HockeyTournamentsListFragment newInstance(long id) {
        HockeyTournamentsListFragment fragment = new HockeyTournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null)
            competitionID = getArguments().getLong(ARG_ID);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new HockeyTournamentAdapter();
    }

    @Override
    protected String getDataKey() {
        return TournamentService.EXTRA_LIST;
    }

    @Override
    protected void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_ALL, getContext());
        intent.putExtra(TournamentService.EXTRA_COMP_ID, competitionID);
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_GET_ALL));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       long compId = getArguments().getLong(ARG_ID, -1);
                                       Intent intent = CreateTournamentActivity.newStartIntent(getContext(), compId, true);

                                       startActivity(intent);
                                   }
                               }
        );

        return fab;
    }

    public class HockeyTournamentAdapter extends TournamentAdapter {
        @Override
        protected void setOnClickListeners(View v) {
            super.setOnClickListeners(v);
            v.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         //TODO na rozkliknuti otevrit tournament, na podrzeni dalsi moznosti
                                     }
                                 }
            );
        }
    }
}
