package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class TournamentAdapter extends AbstractListAdapter<Tournament, TournamentAdapter.TournamentViewHolder> {
    @Override
    public TournamentAdapter.TournamentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tournament, parent, false);
        TournamentViewHolder holder = new TournamentViewHolder(v);

        return holder;
    }

    /**
     * You can overload this view to set various listeners on inflated row view
     * By default none are added
     * @param tournamnetId
     * @param v target view
     * @param tournamentId
     */
    protected void setOnClickListeners(View v, long tournamentId){}

    @Override
    public void onBindViewHolder(TournamentAdapter.TournamentViewHolder holder, int position) {
        Tournament tournament = data.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.name.setText(tournament.getName());
        holder.startDate.setText(R.string.from);
        holder.endDate.setText(R.string.to);

        if (tournament.getStartDate() != null) holder.startDate.append(dateFormat.format(tournament.getStartDate()));
        if (tournament.getEndDate() != null) holder.endDate.append(dateFormat.format(tournament.getEndDate()));

        holder.name.setText(tournament.getName());
        setOnClickListeners(holder.wholeView, tournament.getId());

    }

    public class TournamentViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, startDate, endDate;
        public View wholeView;
        public TournamentViewHolder(View itemView)
        {
            super (itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            startDate = (TextView) itemView.findViewById(R.id.tv_start);
            endDate = (TextView) itemView.findViewById(R.id.tv_end);
            wholeView = itemView;
        }
    }
}
