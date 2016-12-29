package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public abstract class AbstractSelectableListFragment<T extends Parcelable> extends AbstractDataFragment {
    public static AbstractSelectableListFragment newInstance(long id, Class<? extends AbstractSelectableListFragment> clazz){
        AbstractSelectableListFragment fragment = null;
        try {
            Constructor<? extends AbstractSelectableListFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @return Adapter that will be used for this list
     */
    protected abstract AbstractSelectableListAdapter<T, ? extends OneActionViewHolder> getAdapter();

    /**
     *
     * @return key of data passed in intent
     */
    protected abstract String getDataKey();

    /**
     *
     * @return Key of arraylist of ints that determines which items should be selected
     */
    protected abstract String getDataSelectedKey();

    private AbstractSelectableListAdapter<T, ? extends OneActionViewHolder> adapter;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmetview = inflater.inflate(R.layout.fragment_abstract_list, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmetview.findViewById(R.id.recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        return fragmetview;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ArrayList<T> data = intent.getParcelableArrayListExtra(getDataKey());
        ArrayList<Integer> selected = intent.getIntegerArrayListExtra(getDataSelectedKey());

        SparseBooleanArray sparse = new SparseBooleanArray();
        for (int i: selected) sparse.put(i, true);

        adapter.swapData(data, sparse);
    }

    public ArrayList<T> getSelectedData(){
        return adapter.getSelectedItems();
    }
}
