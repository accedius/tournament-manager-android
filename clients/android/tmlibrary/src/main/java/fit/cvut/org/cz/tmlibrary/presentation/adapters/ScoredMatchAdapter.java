package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class ScoredMatchAdapter extends AbstractListAdapter<ScoredMatch,ScoredMatchAdapter.MatchViewHolder> {


    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {

        ScoredMatch m = data.get(position);

        holder.home.setText(m.getHomeName());
        holder.away.setText(m.getAwayName());

        if (!m.isPlayed()) holder.score.setText(R.string.vs);
        else holder.score.setText(String.format("%d:%d", m.getHomeScore(), m.getAwayScore()));

        if (position > 0){

            if (data.get(position-1).getRound() != m.getRound()){
                holder.roundSeparator1.setVisibility(View.VISIBLE);
                holder.roundSeparator2.setVisibility(View.VISIBLE);
            } else if (data.get(position - 1).getPeriod() != m.getPeriod()){
                holder.periodSeparator.setVisibility(View.VISIBLE);
            }
        }

    }

    public class MatchViewHolder extends RecyclerView.ViewHolder
    {
        public TextView home, away, score;
        View periodSeparator, roundSeparator1, roundSeparator2;
        public MatchViewHolder(View itemView)
        {
            super (itemView);
            home = (TextView) itemView.findViewById(R.id.tv_home);
            away = (TextView) itemView.findViewById(R.id.tv_away);
            score = (TextView) itemView.findViewById(R.id.tv_score);

            periodSeparator = itemView.findViewById(R.id.period_separator);
            roundSeparator1 = itemView.findViewById(R.id.round_separator);
            roundSeparator2 = itemView.findViewById(R.id.round_separator2);
        }
    }
}
