package fit.cvut.org.cz.squash.presentation.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.listeners.PlayerDetailOnClickListener;

/**Adapter displaying standings of tournament
 * Created by Vaclav on 7. 4. 2016.
 */
public class StandingsAdapter extends AbstractListAdapter<StandingItem, StandingsAdapter.StandingItemViewHolder> {
    private CompetitionType type;
    private Context context;

    public StandingsAdapter(Context c, CompetitionType t) {
        type = t;
        context = c;
    }

    protected void setOnClickListeners(View v, StandingItem item) {
        // Only individuals are clickable - you can get to Player Detail in Core
        if (type.equals(CompetitionTypes.individuals())) {
            v.setOnClickListener(PlayerDetailOnClickListener.getListener(context, item.id));
        }
    }

    @Override
    public StandingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StandingItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_standings, parent, false));
    }

    @Override
    public void onBindViewHolder(StandingItemViewHolder holder, int position) {
        StandingItem item = data.get(position);
        holder.name.setText(item.name);
        holder.GP.setText(Integer.toString(item.getMatches()));
        holder.W.setText(Integer.toString(item.wins));
        holder.L.setText(Integer.toString(item.losses));
        holder.D.setText(Integer.toString(item.draws));
        holder.points.setText(Integer.toString(item.points));
        holder.sets.setText(String.format("%d:%d", item.setsWon, item.setsLost));
        if (holder.WPER != null) {
            holder.WPER.setText(String.format("%.2f", item.getMatchPer()));
            holder.SPER.setText(String.format("%.2f", item.getSetsPer()));
            holder.B.setText(Integer.toString(item.ballsWon)+":"+Integer.toString(item.ballsLost));
            holder.BPER.setText(String.format("%.2f", item.getBallsPer()));
        }

        setOnClickListeners(holder.itemView, item);
    }

    public class StandingItemViewHolder extends RecyclerView.ViewHolder{
        TextView name, GP, W, L, D, sets, points, WPER, SPER, B, BPER;

        public StandingItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            GP = (TextView) itemView.findViewById(R.id.tv_games_played);
            W = (TextView) itemView.findViewById(R.id.tv_won);
            L = (TextView) itemView.findViewById(R.id.tv_lost);
            D = (TextView) itemView.findViewById(R.id.tv_draws);
            sets = (TextView) itemView.findViewById(R.id.tv_sets_score);
            points = (TextView) itemView.findViewById(R.id.tv_points);
            WPER = (TextView) itemView.findViewById(R.id.tv_won_per);
            SPER = (TextView) itemView.findViewById(R.id.tv_sets_per);
            B = (TextView) itemView.findViewById(R.id.tv_balls_score);
            BPER = (TextView) itemView.findViewById(R.id.tv_balls_per);
        }
    }
}
