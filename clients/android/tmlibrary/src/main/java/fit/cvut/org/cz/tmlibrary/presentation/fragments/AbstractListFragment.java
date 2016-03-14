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
public abstract class AbstractListFragment<T extends Parcelable> extends Fragment {

    public static final String EXTRA_DATA = "extra_data";

    private List<T> data;
    private IProgressInterface progressInterface;
    private RecyclerView recyclerView;
    private AbstractListAdapter adapter;

    protected DataReceiver receiver;

    public AbstractListFragment() {
        // Required empty public constructor
    }

    protected abstract void registerReceivers();
    protected abstract void unregisterReceivers();
    protected abstract void getData();
    protected abstract AbstractListAdapter getAdapter();



    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();
        sendForData();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    private void sendForData(){

        showProgress();
        getData();

    }

    private void showProgress(){if (progressInterface != null) progressInterface.showProgress();}
    private void hideProgress(){if (progressInterface != null) progressInterface.hideProgress();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmetview = inflater.inflate(R.layout.fragment_abstract_list, container, false);

        recyclerView = (RecyclerView) fragmetview.findViewById(R.id.recycler_view);
        data = new ArrayList<T>();
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration dividers = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividers);
        receiver = new DataReceiver();

        return fragmetview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progressInterface = null;
        if (context instanceof IProgressInterface) progressInterface = (IProgressInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        progressInterface = null;
    }

    private class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            hideProgress();
            Toast.makeText(getActivity(), "Data received", Toast.LENGTH_SHORT).show();
            List<T> newData = intent.getParcelableArrayListExtra(EXTRA_DATA);
            //recyclerView.swapAdapter(getAdapter(data), false);
            adapter.swapData(newData);
        }
    }

}
