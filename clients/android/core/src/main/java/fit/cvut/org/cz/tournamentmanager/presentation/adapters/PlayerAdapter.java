package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class PlayerAdapter extends AbstractListAdapter<Player, PlayerAdapter.PlayerViewHolder> {
    private String package_name = "fit.cvut.org.cz.tournamentmanager";
    private String activity_detail_player = "fit.cvut.org.cz.tournamentmanager.presentation.activities.PlayerDetailActivity";

    @Override
    public PlayerAdapter.PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false);
        PlayerViewHolder holder = new PlayerViewHolder(v);
        return holder;
    }

    public PlayerAdapter() {};

    protected void setOnClickListeners(View v, long playerId){}

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = data.get(position);
        holder.name.setText(player.getName());
        holder.email.setText(player.getEmail());

        setOnClickListeners(holder.wholeView, player.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

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
