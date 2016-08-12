package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionAdapter extends AbstractListAdapter<Competition, CompetitionAdapter.CompetitionViewHolder> {
    @Override
    public CompetitionAdapter.CompetitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_competition, parent, false);
        CompetitionViewHolder holder = new CompetitionViewHolder(v);
        return holder;
    }

    public CompetitionAdapter() {};

    protected void setOnClickListeners(View v, long competitionId){}

    @Override
    public void onBindViewHolder(CompetitionAdapter.CompetitionViewHolder holder, int position) {
        Competition competition = data.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");

        holder.name.setText(competition.getName());
        holder.startDate.setText(fit.cvut.org.cz.tmlibrary.R.string.from);
        holder.endDate.setText(fit.cvut.org.cz.tmlibrary.R.string.to);

        if (competition.getStartDate() != null) holder.startDate.append(dateFormat.format(competition.getStartDate()));
        if (competition.getEndDate() != null) holder.endDate.append(dateFormat.format(competition.getEndDate()));

        setOnClickListeners(holder.wholeView, competition.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder{
        public TextView name, startDate, endDate;
        public View wholeView;
        public CompetitionViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            startDate = (TextView) itemView.findViewById(R.id.tv_start);
            endDate = (TextView) itemView.findViewById(R.id.tv_end);
            wholeView = itemView;
        }
    }
}
