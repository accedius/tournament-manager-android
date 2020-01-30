package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.Standing;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class StandingsAdapter extends AbstractListAdapter<Standing, StandingsAdapter.StandingsViewHolder> {
    @Override
    public StandingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StandingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_standings, parent, false));
    }

    /**
     * Method to bind data to View Holder
     * @param holder View Holder
     * @param position position of stats in data array
     */
    @Override
    public void onBindViewHolder(StandingsViewHolder holder, int position) {
        Standing stats = data.get(position);
        holder.name.setText(stats.getName());
        holder.GP.setText(Integer.toString(stats.getMatches()));
        holder.MP.setText(Integer.toString(stats.getMatchPoints()));
        holder.strikes.setText(Integer.toString(stats.getStrikes()));
        holder.spares.setText(Integer.toString(stats.getSpares()));
        holder.points.setText(Integer.toString(stats.getPoints()));

        // Check if we are in landscape
        if(holder.averageStrikes != null) {
            stats.recalculateAverages();
            holder.averageStrikes.setText(String.format("%.2f", stats.getAverageStrikes()));
            holder.averagePoints.setText(String.format("%.2f", stats.getAveragePoints()));
            holder.averageMP.setText(String.format("%.2f", stats.getAverageMatchPoints()));
        }
    }

    /**
     * Standings specific View Holder
     */
    public class StandingsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        TextView name, GP, MP, strikes, spares, points, averageStrikes, averagePoints, averageMP;

        public StandingsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            GP = (TextView) itemView.findViewById(R.id.as_games_played);
            MP = (TextView) itemView.findViewById(R.id.as_match_points);
            strikes = (TextView) itemView.findViewById(R.id.as_strikes);
            spares = (TextView) itemView.findViewById(R.id.as_spares);
            points = (TextView) itemView.findViewById(R.id.as_points);

            averageStrikes = (TextView) itemView.findViewById(R.id.as_avg_st);
            averagePoints = (TextView) itemView.findViewById(R.id.as_avg_p);
            averageMP = (TextView) itemView.findViewById(R.id.as_avg_mp);
        }
    }
}
