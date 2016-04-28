package fit.cvut.org.cz.squash.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class AgregatedStatsAdapter extends AbstractListAdapter<AgregatedStats, AgregatedStatsAdapter.AgregatedStatsViewHolder> {


    @Override
    public AgregatedStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AgregatedStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_agregated_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(AgregatedStatsViewHolder holder, int position) {

        AgregatedStats stats = data.get(position);
        holder.name.setText(stats.playerName);
        holder.W.setText(Integer.toString(stats.won));
        holder.L.setText(Integer.toString(stats.lost));
        holder.D.setText(Integer.toString(stats.draws));
        if (holder.SW != null){
            holder.SW.setText(Integer.toString(stats.setsWon));
            holder.SL.setText(Integer.toString(stats.setsLost));
            holder.BW.setText(Integer.toString(stats.ballsWon));
            holder.BL.setText(Integer.toString(stats.ballsLost));
            holder.SWAVG.setText(String.format("%.2f", stats.setsWonAvg));
            holder.SLAVG.setText(String.format("%.2f", stats.setsLostAvg));
            holder.BWAVG.setText(String.format("%.2f", stats.ballsWonAvg));
            holder.BLAVG.setText(String.format("%.2f", stats.ballsLostAvg));
            holder.WPER.setText(String.format("%.2f", stats.matchWinRate));
            holder.SWPER.setText(String.format("%.2f", stats.setsWinRate));
        }


    }

    public class AgregatedStatsViewHolder extends RecyclerView.ViewHolder{

        public long id;
        TextView name, W, L, D, SW, SL, SWAVG, SLAVG, BW, BL, BWAVG, BLAVG, WPER, SWPER;

        public AgregatedStatsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            W = (TextView) itemView.findViewById(R.id.tv_won);
            L = (TextView) itemView.findViewById(R.id.tv_lost);
            D = (TextView) itemView.findViewById(R.id.tv_draws);
            SW = (TextView) itemView.findViewById(R.id.tv_sets_won);
            SL = (TextView) itemView.findViewById(R.id.tv_sets_lost);
            SWAVG = (TextView) itemView.findViewById(R.id.tv_sets_won_avg);
            SLAVG = (TextView) itemView.findViewById(R.id.tv_sets_lost_avg);
            BW = (TextView) itemView.findViewById(R.id.tv_balls_won);
            BL = (TextView) itemView.findViewById(R.id.tv_balls_lost);
            BLAVG = (TextView) itemView.findViewById(R.id.tv_balls_lost_avg);
            BWAVG = (TextView) itemView.findViewById(R.id.tv_balls_won_avg);
            WPER = (TextView) itemView.findViewById(R.id.tv_won_per);
            SWPER = (TextView) itemView.findViewById(R.id.tv_sets_per);


        }
    }
}
