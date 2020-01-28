package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;

public class SelectTeamPlayersAdapter extends AbstractSelectableListAdapter<Player, SimpleOneActionViewHolder> {
    @Override
    protected void bindView(SimpleOneActionViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
    }

    @NonNull
    @Override
    public SimpleOneActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(fit.cvut.org.cz.tmlibrary.R.layout.row_select_item, parent, false);
        return new SimpleOneActionViewHolder(v, this);
    }
}
