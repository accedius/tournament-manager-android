package fit.cvut.org.cz.hockey.presentation.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.HockeyScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class HockeyScoredMatchAdapter extends AbstractListAdapter<HockeyScoredMatch,HockeyScoredMatchAdapter.MatchViewHolder> {

    protected  Resources res;

    public HockeyScoredMatchAdapter(Resources res) {
        this.res = res;
    }

    protected void setOnClickListeners(View v, HockeyScoredMatch match, int position){}

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        HockeyScoredMatch m = data.get(position);
        holder.home.setText(m.getScoredMatch().getHomeName());
        holder.away.setText(m.getScoredMatch().getAwayName());

        if (!m.getScoredMatch().isPlayed())
            holder.score.setText(R.string.vs);
        else {
            if (m.getMatchScore().isShootouts()) {
                holder.score.setText(String.format("%d:%d %s", m.getScoredMatch().getHomeScore(), m.getScoredMatch().getAwayScore(), res.getString(R.string.so)));
            } else if (m.getMatchScore().isOvertime()) {
                holder.score.setText(String.format("%d:%d %s", m.getScoredMatch().getHomeScore(), m.getScoredMatch().getAwayScore(), res.getString(R.string.ot)));
            } else {
                holder.score.setText(String.format("%d:%d", m.getScoredMatch().getHomeScore(), m.getScoredMatch().getAwayScore()));
            }
        }

        if (position > 0){
            holder.roundSeparator1.setVisibility(View.GONE);
            holder.roundSeparator2.setVisibility(View.GONE);
            holder.periodSeparator.setVisibility(View.GONE);
            if (data.get(position-1).getScoredMatch().getRound() != m.getScoredMatch().getRound()){
                holder.roundSeparator1.setVisibility(View.VISIBLE);
                holder.roundSeparator2.setVisibility(View.VISIBLE);
            } else if (data.get(position - 1).getScoredMatch().getPeriod() != m.getScoredMatch().getPeriod()){
                holder.periodSeparator.setVisibility(View.VISIBLE);
            }
        }

        setOnClickListeners(holder.card, m, position);
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        public TextView home, away, score;
        View periodSeparator, roundSeparator1, roundSeparator2, card;
        public MatchViewHolder(View itemView) {
            super (itemView);
            home = (TextView) itemView.findViewById(R.id.tv_home);
            away = (TextView) itemView.findViewById(R.id.tv_away);
            score = (TextView) itemView.findViewById(R.id.tv_score);

            periodSeparator = itemView.findViewById(R.id.period_separator);
            roundSeparator1 = itemView.findViewById(R.id.round_separator);
            roundSeparator2 = itemView.findViewById(R.id.round_separator2);
            card = itemView.findViewById(R.id.card);
        }
    }
}
