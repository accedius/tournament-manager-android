package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionAdapter extends AbstractListAdapter<Competition, CompetitionAdapter.CompetitonViewHolder> {

    @Override
    public CompetitonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_competition, parent, false);
        CompetitonViewHolder holder = new CompetitonViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(CompetitonViewHolder holder, int position) {

        Competition competition = data.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.name.setText(competition.getName());
        holder.startDate.setText("from: " + dateFormat.format(competition.getStartDate()));
        holder.endDate.setText("to: " + dateFormat.format(competition.getEndDate()));
        holder.name.setText(competition.getName());

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class CompetitonViewHolder extends RecyclerView.ViewHolder{

        public TextView name, startDate, endDate;

        public CompetitonViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            startDate = (TextView) itemView.findViewById(R.id.tv_start);
            endDate = (TextView) itemView.findViewById(R.id.tv_end);
        }
    }
}
