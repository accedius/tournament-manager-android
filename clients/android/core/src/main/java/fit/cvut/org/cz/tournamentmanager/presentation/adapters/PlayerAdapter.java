package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    private Context c;
    private View v;
    private String package_name = "fit.cvut.org.cz.tournamentmanager";
    private String activity_detail_player = "fit.cvut.org.cz.tournamentmanager.presentation.activities.PlayerDetailActivity";

    public PlayerAdapter(Context c) {
        this.c = c;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false);
        PlayerViewHolder holder = new PlayerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = data.get(position);
        holder.name.setText(player.getName());
        holder.email.setText(player.getEmail());

        final long id = player.getId();

        // TODO po spuštění aktivity zachytává AbstractListFragment akci pro získání info o hráči, což by neměl
        /*
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(package_name, activity_detail_player);
                Bundle b = new Bundle();
                // TODO string konstanta, definovat pravděpodobně někde bokem
                b.putLong("player_id", id);
                intent.putExtras(b);
                c.startActivity(intent);
            }
        });*/

        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Edit");
                menu.add("Delete");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder{
        public TextView name, email;
        public PlayerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            email = (TextView) itemView.findViewById(R.id.tv_email);
        }
    }
}
