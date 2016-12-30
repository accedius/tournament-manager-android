package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportPlayerAdapter extends AbstractListAdapter<PlayerImportInfo, ImportPlayerAdapter.ImportPlayerViewHolder> {
    @Override
    public ImportPlayerAdapter.ImportPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_import_player, parent, false);
        ImportPlayerViewHolder holder = new ImportPlayerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImportPlayerAdapter.ImportPlayerViewHolder holder, int position) {
        PlayerImportInfo player = data.get(position);

        holder.name.setText(player.getName());
        holder.email.setText(player.getEmail());
    }

    public class ImportPlayerViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public View wholeView;
        public ImportPlayerViewHolder(View itemView) {
            super (itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            email = (TextView) itemView.findViewById(R.id.tv_email);
            wholeView = itemView;
        }
    }
}
