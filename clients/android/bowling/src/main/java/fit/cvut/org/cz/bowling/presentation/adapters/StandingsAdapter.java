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
        if (holder.Wot == null) {
            holder.W.setText(Integer.toString(stats.getTotalWins()));
            holder.L.setText(Integer.toString(stats.getTotalLosses()));
        }
        holder.D.setText(Integer.toString(stats.getDraws()));
        holder.SCORE.setText(Integer.toString(stats.getGoalsGiven())+":"+Long.toString(stats.getGoalsReceived()));
        holder.GP.setText(Integer.toString(stats.getMatches()));
        holder.P.setText(Integer.toString(stats.getPoints()));

        if (holder.Wot != null) {
            holder.W.setText(Integer.toString(stats.getWins()));
            holder.Wot.setText(Integer.toString(stats.getWinsOt()));
            holder.Wso.setText(Integer.toString(stats.getWinsSo()));
            holder.L.setText(Integer.toString(stats.getLosses()));
            holder.Lot.setText(Integer.toString(stats.getLossesOt()));
            holder.Lso.setText(Integer.toString(stats.getLossesSo()));
        }
    }

    /**
     * Standings specific View Holder
     */
    public class StandingsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        TextView name, GP, W, Wot, Wso, L, Lot, Lso, D, SCORE, P;

        public StandingsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            GP = (TextView) itemView.findViewById(R.id.as_games_played);
            W = (TextView) itemView.findViewById(R.id.as_wins);
            Wot = (TextView) itemView.findViewById(R.id.as_wins_ot);
            Wso = (TextView) itemView.findViewById(R.id.as_wins_so);
            L = (TextView) itemView.findViewById(R.id.as_losses);
            Lot = (TextView) itemView.findViewById(R.id.as_losses_ot);
            Lso = (TextView) itemView.findViewById(R.id.as_losses_so);
            D = (TextView) itemView.findViewById(R.id.as_draws);
            SCORE = (TextView) itemView.findViewById(R.id.as_score);
            P = (TextView) itemView.findViewById(R.id.as_points);
        }
    }
}
