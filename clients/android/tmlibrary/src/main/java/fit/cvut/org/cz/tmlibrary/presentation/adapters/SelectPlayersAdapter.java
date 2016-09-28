package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public class SelectPlayersAdapter extends AbstractSelectableListAdapter<Player, SimpleOneActionViewHolder> {
    @Override
    protected void bindView(SimpleOneActionViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
    }

    @Override
    public SimpleOneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_player, parent, false);
        return new SimpleOneActionViewHolder(v, this);
    }

}
