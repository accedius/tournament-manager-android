package fit.cvut.org.cz.hockey.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class AgregatedStatisticsAdapter extends AbstractListAdapter<AgregatedStatistics, AgregatedStatisticsAdapter.AgregatedStatisticsViewHolder> {


    @Override
    public AgregatedStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AgregatedStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agreg_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(AgregatedStatisticsViewHolder holder, int position) {
        AgregatedStatistics stats = data.get( position );
        holder.name.setText(stats.getPlayerName());
        holder.W.append(Long.toString(stats.getWins()));
        holder.L.append(Long.toString(stats.getLosses()));
        holder.D.append(Long.toString(stats.getDraws()));
        holder.G.append(Long.toString(stats.getGoals()));
        holder.A.append(Long.toString(stats.getAssists()));
        if( holder.M != null ) {
            holder.M.append(Long.toString(stats.getMatches()));
            holder.P.append(Long.toString(stats.getPoints()));
            holder.PMP.append(Long.toString(stats.getPlusMinusPoints()));
            holder.TP.append(Long.toString(stats.getTeamPoints()));
            holder.AG.append(String.format("%.2f", stats.getAvgGoals()));
            holder.AP.append(String.format("%.2f", stats.getAvgPoints()));
            holder.APMP.append(String.format("%.2f", stats.getAvgPlusMinus()));
            holder.ATP.append(String.format("%.2f", stats.getAvgTeamPoints()));
        }
    }

    public class AgregatedStatisticsViewHolder extends RecyclerView.ViewHolder
    {
        public long id;
        TextView name, M, W, L, D, G, A, P, PMP, TP, AG, AP, APMP, ATP;

        public AgregatedStatisticsViewHolder(View itemView) {
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

        }
    }
}
