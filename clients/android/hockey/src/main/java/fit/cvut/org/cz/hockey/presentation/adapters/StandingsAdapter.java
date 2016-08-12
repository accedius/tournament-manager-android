package fit.cvut.org.cz.hockey.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Adapter holding standings in tournament
 * Created by atgot_000 on 19. 4. 2016.
 */
public class StandingsAdapter extends AbstractListAdapter<Standing, StandingsAdapter.StandingsViewHolder> {


    @Override
    public StandingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StandingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_standings, parent, false));
    }

    @Override
    public void onBindViewHolder(StandingsViewHolder holder, int position) {
        Standing stats = data.get( position );
        holder.name.setText(stats.getName());
        holder.W.setText(Long.toString(stats.getWins()));
        holder.L.setText(Long.toString(stats.getLosses()));
        holder.D.setText(Long.toString(stats.getDraws()));
        holder.SCORE.setText(Long.toString(stats.getGoalsGiven())+":"+Long.toString(stats.getGoalsReceived()));
        holder.P.setText(Long.toString(stats.getPoints()));
    }

    public class StandingsViewHolder extends RecyclerView.ViewHolder
    {
        public long id;
        TextView name, W, L, D, SCORE, P;

        public StandingsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            W = (TextView) itemView.findViewById(R.id.as_wins);
            L = (TextView) itemView.findViewById(R.id.as_losses);
            D = (TextView) itemView.findViewById(R.id.as_draws);
            SCORE = (TextView) itemView.findViewById(R.id.as_score);
            P = (TextView) itemView.findViewById(R.id.as_points);
        }
    }
}
