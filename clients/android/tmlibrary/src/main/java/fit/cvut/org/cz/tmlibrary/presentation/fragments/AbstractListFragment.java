package fit.cvut.org.cz.tmlibrary.presentation.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.decorators.DividerItemDecoration;
import fit.cvut.org.cz.tmlibrary.presentation.interfaces.IProgressInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractListFragment<T extends Parcelable> extends AbstractDataFragment {

    private AbstractListAdapter adapter;
    protected RecyclerView recyclerView;
    protected FloatingActionButton fab = null;

    public AbstractListFragment() {
        // Required empty public constructor
    }

    protected abstract AbstractListAdapter getAdapter();
    protected abstract String getDataKey();

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_abstract_list, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final FloatingActionButton fab = getFAB((ViewGroup) fragmentView);
        if (fab != null){
            this.fab = fab;
            //fab.hide();
            ((ViewGroup) fragmentView).addView(fab);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        fab.show();

                    else fab.hide();
                }


            });
        }

        return fragmentView;
    }

    protected FloatingActionButton getFAB(ViewGroup parent){return null;}

    @Override
    protected void customOnResume() {
        //if (fab != null) fab.show();
        super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        //if (fab != null) fab.hide();
        super.customOnPause();
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ArrayList<T> data = intent.getParcelableArrayListExtra(getDataKey());
        Log.d("ADF", "Received data, len: "+data.size());
        adapter.swapData(data);
    }
}
