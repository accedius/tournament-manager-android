package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;

/**
 * Adapter for displaying selectable list of Players.
 */
public class SelectPlayersAdapter extends AbstractSelectableListAdapter<Player, SimpleOneActionViewHolder> {
    @Override
    protected void bindView(SimpleOneActionViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
    }

    @Override
    public SimpleOneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_item, parent, false);
        return new SimpleOneActionViewHolder(v, this);
    }

}
