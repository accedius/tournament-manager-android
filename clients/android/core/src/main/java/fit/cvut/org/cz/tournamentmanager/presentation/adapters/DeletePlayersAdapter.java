package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractDeletableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 19. 3. 2016.
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
