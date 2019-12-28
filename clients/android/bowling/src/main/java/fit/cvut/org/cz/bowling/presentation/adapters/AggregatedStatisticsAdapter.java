package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class AggregatedStatisticsAdapter extends AbstractListAdapter<AggregatedStatistics, AggregatedStatisticsAdapter.AggregatedStatisticsViewHolder> {
    @Override
    public AggregatedStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AggregatedStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aggreg_stats, parent, false));
    }

    /**
     * Method to bind data to View Holder
     * @param holder View Holder
     * @param position position of aggregated stats in data array
     */
    @Override
    public void onBindViewHolder(AggregatedStatisticsViewHolder holder, int position) {
        AggregatedStatistics stats = data.get(position);
        holder.name.setText(stats.getPlayerName());
        holder.GP.setText(Long.toString(stats.getMatches()));
        holder.ST.setText(Long.toString(stats.getStrikes()));
        holder.SP.setText(Long.toString(stats.getSpares()));
        holder.P.setText(Long.toString(stats.getPoints()));
        if (holder.TP != null) {
            holder.TP.setText(Long.toString(stats.getTeamPoints()));
            holder.AST.setText(String.format("%.2f", stats.getAvgStrikes()));
            holder.ASP.setText(String.format("%.2f", stats.getAvgPoints()));
            holder.ATP.setText(String.format("%.2f", stats.getAvgTeamPoints()));
        }
        setOnClickListeners(holder.wholeView, stats.getPlayerId(), stats.getPlayerName());
    }

    /**
     * override this function to set on click listeners for rows
     * @param v view of the row
     * @param playerId id of player that was clicked
     */
    protected void setOnClickListeners(View v, long playerId, String name) {};


    /**
     * Aggregated Statistics specific View Holder
     */
    public class AggregatedStatisticsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        TextView name, GP, ST, SP, P, TP, AST, ASP, ATP;
        public View wholeView;

        public AggregatedStatisticsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            GP = (TextView) itemView.findViewById(R.id.as_games_played);
            ST = (TextView) itemView.findViewById(R.id.as_strikes);
            SP = (TextView) itemView.findViewById(R.id.as_spares);
            P = (TextView) itemView.findViewById(R.id.as_points);

            TP = (TextView) itemView.findViewById(R.id.as_team_points);
            AST = (TextView) itemView.findViewById(R.id.as_avg_st);
            ASP = (TextView) itemView.findViewById(R.id.as_avg_sp);
            ATP = (TextView) itemView.findViewById(R.id.as_avg_tp);
            wholeView = itemView;
        }
    }
}
