package fit.cvut.org.cz.hockey.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Adapter holding aggregated statistics
 * Created by atgot_000 on 8. 4. 2016.
 */
public class AggregatedStatisticsAdapter extends AbstractListAdapter<AggregatedStatistics, AggregatedStatisticsAdapter.AggregatedStatisticsViewHolder> {


    @Override
    public AggregatedStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AggregatedStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aggreg_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(AggregatedStatisticsViewHolder holder, int position) {
        AggregatedStatistics stats = data.get( position );
        holder.name.setText(stats.getPlayerName());
        holder.W.setText(Long.toString(stats.getWins()));
        holder.L.setText(Long.toString(stats.getLosses()));
        holder.D.setText(Long.toString(stats.getDraws()));
        holder.G.setText(Long.toString(stats.getGoals()));
        holder.A.setText(Long.toString(stats.getAssists()));
        if( holder.M != null ) {
            holder.M.setText(Long.toString(stats.getMatches()));
            holder.P.setText(Long.toString(stats.getPoints()));
            holder.PMP.setText(Long.toString(stats.getPlusMinusPoints()));
            holder.TP.setText(Long.toString(stats.getTeamPoints()));
            holder.I.setText(Long.toString(stats.getSaves()));
            holder.AG.setText(String.format("%.2f", stats.getAvgGoals()));
            holder.AP.setText(String.format("%.2f", stats.getAvgPoints()));
            holder.APMP.setText(String.format("%.2f", stats.getAvgPlusMinus()));
            holder.ATP.setText(String.format("%.2f", stats.getAvgTeamPoints()));
        }
        setOnClickListeners(holder.wholeView, stats.getPlayerID());
    }

    /**
     * override this function to set on click listeners for rows
     * @param v view of the row
     * @param playerId id of player that was clicked
     */
    protected void setOnClickListeners( View v, long playerId ) {};

    public class AggregatedStatisticsViewHolder extends RecyclerView.ViewHolder
    {
        public long id;
        TextView name, M, W, L, D, G, A, P, PMP, TP, AG, AP, APMP, ATP, I;
        public View wholeView;

        public AggregatedStatisticsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            M = (TextView) itemView.findViewById(R.id.as_games);
            W = (TextView) itemView.findViewById(R.id.as_wins);
            L = (TextView) itemView.findViewById(R.id.as_losses);
            D = (TextView) itemView.findViewById(R.id.as_draws);
            G = (TextView) itemView.findViewById(R.id.as_goals);
            A = (TextView) itemView.findViewById(R.id.as_assists);
            P = (TextView) itemView.findViewById(R.id.as_points);
            PMP = (TextView) itemView.findViewById(R.id.as_pmp);
            TP = (TextView) itemView.findViewById(R.id.as_tp);
            AG = (TextView) itemView.findViewById(R.id.as_avg_g);
            AP = (TextView) itemView.findViewById(R.id.as_avg_p);
            APMP = (TextView) itemView.findViewById(R.id.as_avg_pmp);
            ATP = (TextView) itemView.findViewById(R.id.as_avg_tp);
            I = (TextView) itemView.findViewById(R.id.as_i);
            wholeView = itemView;
        }
    }
}
