package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class PlayerAdapter extends AbstractListAdapter<DPlayer, PlayerAdapter.PlayerViewHolder> {

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false);
        PlayerViewHolder holder = new PlayerViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = new Player(data.get(position));

        holder.name.setText(player.getName());
        holder.email.setText(player.getEmail());
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
