package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.presentation.adapters.SetsAdapter;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.dialogs.AdapterDialog;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Allows user to manage sets for match
 * Created by Vaclav on 24. 4. 2016.
 */
public class SetsFragment extends AbstractListFragment<SetRowItem> {
    private Fragment thisFragment;

    public static SetsFragment newInstance(long id, boolean played){
        SetsFragment fragment = new SetsFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putBoolean(ExtraConstants.EXTRA_PLAYED, played);
        fragment.setArguments(args);
        return fragment;
    }

    public SetsAdapter adapter;
    private boolean askForData = false, receiverRegistered = false;
    private ArrayList<SetRowItem> data = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(ExtraConstants.EXTRA_DATA);
            askForData = savedInstanceState.getBoolean(ExtraConstants.EXTRA_ASK);
        } else {
            askForData = getArguments().getBoolean(ExtraConstants.EXTRA_PLAYED);
            receiverRegistered = askForData;
        }
        thisFragment = this;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        super.bindDataOnView(intent);
        data = intent.getParcelableArrayListExtra(getDataKey());
        askForData = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        data = new ArrayList<>(adapter.getData());
        outState.putParcelableArrayList(ExtraConstants.EXTRA_DATA, data);
        outState.putBoolean(ExtraConstants.EXTRA_ASK, askForData);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter = new SetsAdapter(getResources()){
            @Override
            protected void setOnClickListeners(View itemView, final int position, final String title) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AdapterDialog dialog = AdapterDialog.newInstance(position, 0, title);
                        dialog.setTargetFragment(thisFragment, 0);
                        dialog.show(getFragmentManager(), "DELETE_DIALOG");
                        return false;
                    }
                });
            }
        };
        return adapter;
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_SETS;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GET_MATCH_SETS, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_GET_MATCH_SETS);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_GET_MATCH_SETS));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public void customOnResume() {
        if (data != null) adapter.swapData(data);
        if (askForData) super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (receiverRegistered) super.customOnPause();
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(fit.cvut.org.cz.tmlibrary.R.layout.fab_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new SetRowItem());
                recyclerView = (RecyclerView) getActivity().findViewById(fit.cvut.org.cz.tmlibrary.R.id.recycler_view);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
        });
        return fab;
    }

    public List<SetRowItem> getSets() {
        return adapter.getData();
    }
    public boolean hasErrors() {return adapter.hasErrors();}
}
