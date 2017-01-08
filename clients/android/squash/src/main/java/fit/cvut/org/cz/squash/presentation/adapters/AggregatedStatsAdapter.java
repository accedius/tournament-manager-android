package fit.cvut.org.cz.squash.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**Adapter holding aggregated statistics
 * Created by Vaclav on 7. 4. 2016.
 */
public class AggregatedStatsAdapter extends AbstractListAdapter<SAggregatedStats, AggregatedStatsAdapter.AggregatedStatsViewHolder> {
    @Override
    public AggregatedStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AggregatedStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_aggregated_stats, parent, false));
    }

    protected void setOnClickListeners(View v, SAggregatedStats item, int position, String name) {}

    @Override
    public void onBindViewHolder(AggregatedStatsViewHolder holder, int position) {
        SAggregatedStats stats = data.get(position);
        holder.name.setText(stats.playerName);
        holder.GP.setText(Integer.toString(stats.games_played));
        holder.P.setText(Integer.toString(stats.points));
        holder.W.setText(Integer.toString(stats.won));
        holder.L.setText(Integer.toString(stats.lost));
        holder.D.setText(Integer.toString(stats.draws));
        if (holder.WPER != null) {
            holder.WPER.setText(String.format("%.2f", stats.matchWinRate));
            holder.S.setText(Integer.toString(stats.setsWon)+":"+Integer.toString(stats.setsLost));
            holder.SPER.setText(String.format("%.2f", stats.getSetsPer()));
            holder.B.setText(Integer.toString(stats.ballsWon)+":"+Integer.toString(stats.ballsLost));
            holder.BPER.setText(String.format("%.2f", stats.getBallsPer()));
        }
        setOnClickListeners(holder.wholeView, stats, position, stats.playerName);
    }

    public class AggregatedStatsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public View wholeView;
        TextView name, GP, P, W, L, D, WPER, S, SPER, B, BPER;

        public AggregatedStatsViewHolder(View itemView) {
            super(itemView);
            wholeView = itemView;

            name = (TextView) itemView.findViewById(R.id.tv_name);
            GP = (TextView) itemView.findViewById(R.id.tv_games_played);
            P = (TextView) itemView.findViewById(R.id.tv_points);
            W = (TextView) itemView.findViewById(R.id.tv_won);
            L = (TextView) itemView.findViewById(R.id.tv_lost);
            D = (TextView) itemView.findViewById(R.id.tv_draws);
            WPER = (TextView) itemView.findViewById(R.id.tv_won_per);
            S = (TextView) itemView.findViewById(R.id.tv_sets_score);
            SPER = (TextView) itemView.findViewById(R.id.tv_sets_per);
            B = (TextView) itemView.findViewById(R.id.tv_balls_score);
            BPER = (TextView) itemView.findViewById(R.id.tv_balls_per);
        }
    }
}
