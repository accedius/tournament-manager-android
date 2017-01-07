package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Adapter for displaying list of Player.
 */
public class PlayerAdapter extends AbstractListAdapter<Player, PlayerAdapter.PlayerViewHolder> {
    @Override
    public PlayerAdapter.PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false);
        PlayerViewHolder holder = new PlayerViewHolder(v);
        return holder;
    }

    /**
     * On click listener setter to be overridden, when necessary.
     * @param v View
     * @param playerId id of Player
     * @param position position in list
     * @param name Player name
     */
    protected void setOnClickListeners(View v, long playerId, int position, String name){}

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = data.get(position);
        holder.name.setText(player.getName());
        holder.email.setText(player.getEmail());

        setOnClickListeners(holder.wholeView, player.getId(), position, player.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * View holder for Player.
     */
    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        public TextView name, email;
        public View wholeView;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            email = (TextView) itemView.findViewById(R.id.tv_email);
            wholeView = itemView;
        }
    }
}
