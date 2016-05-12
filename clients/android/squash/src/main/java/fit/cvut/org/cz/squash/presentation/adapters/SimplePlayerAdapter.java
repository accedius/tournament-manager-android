package fit.cvut.org.cz.squash.presentation.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.dialogs.DeleteDialog;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by Vaclav on 4. 5. 2016.
 */
public class SimplePlayerAdapter extends AbstractListAdapter<Player, SimplePlayerAdapter.PlayerVH> {

    @Override
    public PlayerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayerVH holder, final int position) {
        holder.name.setText(data.get(position).getName());

        holder.wholeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeleteDialog d = new DeleteDialog(){
                    @Override
                    protected DialogInterface.OnClickListener supplyListener() {
                        return new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(position);
                            }
                        };
                    }
                };
                Context context = holder.wholeView.getContext();
                d.setRetainInstance(true);
                if (context instanceof FragmentActivity) d.show(((FragmentActivity) context).getSupportFragmentManager(), "delete_tag");
                return true;
            }
        });
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
