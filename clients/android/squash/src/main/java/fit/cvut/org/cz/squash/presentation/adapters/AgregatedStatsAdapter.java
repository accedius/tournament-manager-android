package fit.cvut.org.cz.squash.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.buisness.entities.AgregatedStats;
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
        holder.name.setText(stats.getPlayerName());
        holder.W.append(Integer.toString(stats.getWon()));
        holder.L.append(Integer.toString(stats.getLost()));
        holder.D.append(Integer.toString(stats.getDraws()));
        if (holder.SW != null){
            holder.SW.append(Integer.toString(stats.getSetsWon()));
            holder.SL.append(Integer.toString(stats.getSetsLost()));
            holder.BW.append(Integer.toString(stats.getBallsWon()));
            holder.BL.append(Integer.toString(stats.getBallsLost()));
            holder.SWAVG.append(String.format("%.2f", stats.getSetsWonAvg()));
            holder.SLAVG.append(String.format("%.2f", stats.getSetsLostAvg()));
            holder.BWAVG.append(String.format("%.2f", stats.getBallsWonAvg()));
            holder.BLAVG.append(String.format("%.2f", stats.getBallsLostAvg()));
            holder.WPER.append(String.format("%.2f", stats.getMatchWinRate()));
            holder.SWPER.append(String.format("%.2f", stats.getSetsWinRate()));
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
