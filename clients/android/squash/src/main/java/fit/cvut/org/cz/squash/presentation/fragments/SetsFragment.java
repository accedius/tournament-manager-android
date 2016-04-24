package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.presentation.adapters.SetsAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class SetsFragment extends AbstractListFragment<SetRowItem> {

    private SetsAdapter adapter;

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter = new SetsAdapter();
        return adapter;
    }

    @Override
    protected String getDataKey() {
        return null;
    }

    @Override
    protected void askForData() {

    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void registerReceivers() {

    }

    @Override
    protected void unregisterReceivers() {

    }

    @Override
    protected void customOnResume() {

    }

    @Override
    protected void customOnPause() {

    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {

        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(fit.cvut.org.cz.tmlibrary.R.layout.fab_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new SetRowItem());
            }
        });
        return fab;
    }


    public ArrayList<SetRowItem> getSets() {
        return adapter.getData();
    }
}
