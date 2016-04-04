package fit.cvut.org.cz.tmlibrary.presentation.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public AbstractListFragment() {
        // Required empty public constructor
    }

    protected abstract AbstractListAdapter getAdapter();
    protected abstract String getDataKey();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmetview = inflater.inflate(R.layout.fragment_abstract_list, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmetview.findViewById(R.id.recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        RecyclerView.ItemDecoration dividers = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
//        recyclerView.addItemDecoration(dividers);

        return fragmetview;
    }

    @Override
    protected void bindDataOnView(Intent intent) {

        List<T> data = intent.getParcelableArrayListExtra(getDataKey());
        adapter.swapData(data);
    }
}
