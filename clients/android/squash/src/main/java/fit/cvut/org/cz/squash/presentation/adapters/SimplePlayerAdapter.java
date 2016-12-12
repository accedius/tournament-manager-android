package fit.cvut.org.cz.squash.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Adapter holding player names
 * Created by Vaclav on 4. 5. 2016.
 */
public class SimplePlayerAdapter extends AbstractListAdapter<PlayerStat, SimplePlayerAdapter.PlayerVH> {
    @Override
    public PlayerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false));
    }

    protected void setOnClickListeners(View itemView, int position, String name) {}

    public void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    @Override
    public void onBindViewHolder(final PlayerVH holder, int position) {
        holder.name.setText(data.get(position).getName());
        setOnClickListeners(holder.itemView, position, data.get(position).getName());
    }

    public class PlayerVH extends RecyclerView.ViewHolder{
        public TextView name;
        public View wholeView;

        public PlayerVH(View itemView) {
            super(itemView);
            wholeView = itemView;
            name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
