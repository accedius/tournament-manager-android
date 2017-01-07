package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;

/**
 * Adapter for displaying list of deletable Players.
 */
public class DeletePlayersAdapter extends AbstractDeletableListAdapter<Player, SimpleOneActionViewHolder> {
    @Override
    public SimpleOneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleOneActionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_delete_player, parent, false), this);
    }

    @Override
    public void onBindViewHolder(SimpleOneActionViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
    }
}
