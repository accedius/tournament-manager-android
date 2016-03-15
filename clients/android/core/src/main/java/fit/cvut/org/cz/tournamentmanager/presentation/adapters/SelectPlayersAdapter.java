package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.vh.SelectPlayersViewHolder;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public class SelectPlayersAdapter extends AbstractSelectableListAdapter<Player, SelectPlayersViewHolder> {


    @Override
    protected void bindView(SelectPlayersViewHolder holder, int position) {

    }

    @Override
    public SelectPlayersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        return  new SelectPlayersViewHolder(v, this);
    }


}
