package fit.cvut.org.cz.hockey.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by atgot_000 on 23. 4. 2016.
 */
public class MatchStatisticsAdapter extends AbstractListAdapter<MatchPlayerStatistic, MatchStatisticsAdapter.MatchStatisticsViewHolder> {


    @Override
    public MatchStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchStatisticsViewHolder holder, int position) {
        MatchPlayerStatistic stats = data.get( position );
        holder.name.setText(stats.getName());
        holder.G.setText(Long.toString(stats.getGoals()));
        holder.A.setText(Long.toString(stats.getAssists()));
        holder.I.setText(Long.toString(stats.getInterventions()));
        holder.PMP.setText(Long.toString(stats.getPlusMinusPoints()));
    }

    public class MatchStatisticsViewHolder extends RecyclerView.ViewHolder
    {
        public long id;
        TextView name, G, A, PMP, I;

        public MatchStatisticsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            G = (TextView) itemView.findViewById(R.id.as_goals);
            A = (TextView) itemView.findViewById(R.id.as_assists);
            PMP = (TextView) itemView.findViewById(R.id.as_pmp);
            I = (TextView) itemView.findViewById(R.id.as_interventions);

        }
    }
}
