package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.entities.Conflict;

/**
 * Created by kevin on 24.10.2016.
 */
public class ConflictAdapter extends AbstractListAdapter<Conflict, ConflictAdapter.ConflictViewHolder> {
    @Override
    public ConflictAdapter.ConflictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conflict, parent, false);
        ConflictViewHolder holder = new ConflictViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ConflictAdapter.ConflictViewHolder holder, int position) {
        Conflict conflict = data.get(position);
        holder.title.setText(conflict.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ConflictViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public View wholeView;
        public ConflictViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            wholeView = itemView;
        }
    }
}
