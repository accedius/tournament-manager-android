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
    private Resources res;

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
        holder.W.setText(Integer.toString(item.wins));
        holder.L.setText(Integer.toString(item.losses));
        holder.D.setText(Integer.toString(item.draws));
        holder.points.setText(Integer.toString(item.points));
        holder.score.setText(String.format("%d:%d", item.setsWon, item.setsLost));

        setOnClickListeners(holder.itemView, item);
    }

    public class StandingItemViewHolder extends RecyclerView.ViewHolder{
        TextView name, W, L, D, score, points;

        public StandingItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            W = (TextView) itemView.findViewById(R.id.tv_won);
            L = (TextView) itemView.findViewById(R.id.tv_lost);
            D = (TextView) itemView.findViewById(R.id.tv_draws);
            score = (TextView) itemView.findViewById(R.id.tv_score);
            points = (TextView) itemView.findViewById(R.id.tv_points);
        }
    }
}
