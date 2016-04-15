package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractDeletableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DeletePlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;

/**
 * Created by Vaclav on 15. 4. 2016.
 */
public class DeletablePlayerListFragment extends AbstractListFragment<Player> {

    private AbstractDeletableListAdapter<Player, ? extends OneActionViewHolder> adapter;
    private FloatingActionButton button;

    public FloatingActionButton getButton() {
        return button;
    }

    public void setButton(FloatingActionButton button) {
        this.button = button;
    }

    @Override
    protected AbstractListAdapter getAdapter() {

        return adapter;
    }

    public void supplyData(ArrayList<Player> players){
        adapter.swapData(players);
    }

    public ArrayList<Player> getData(){
        return adapter.getData();
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        button = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);
        return button;
    }

    //We do not need this methods since we are going to supply data manually
    @Override
    protected String getDataKey() { return null; }
    @Override
    protected void askForData() {}
    @Override
    protected boolean isDataSourceWorking() { return true;}
    @Override
    protected void registerReceivers() {}
    @Override
    protected void unregisterReceivers() {}
}
