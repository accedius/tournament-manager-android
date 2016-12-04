package fit.cvut.org.cz.hockey.presentation.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class HockeyScoredMatchAdapter extends AbstractListAdapter<Match,HockeyScoredMatchAdapter.MatchViewHolder> {
    protected Resources res;

    public HockeyScoredMatchAdapter(Resources res) {
        this.res = res;
    }

    protected void setOnClickListeners(View v, Match match, int position, String title){}

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match m = data.get(position);
        holder.home.setText(m.getHomeName());
        holder.away.setText(m.getAwayName());

        if (m.isPlayed()) {
            if (m.isShootouts()) {
                holder.score.setText(String.format("%d:%d %s", m.getHomeScore(), m.getAwayScore(), res.getString(R.string.so)));
            } else if (m.isOvertime()) {
                holder.score.setText(String.format("%d:%d %s", m.getHomeScore(), m.getAwayScore(), res.getString(R.string.ot)));
            } else {
                holder.score.setText(String.format("%d:%d", m.getHomeScore(), m.getAwayScore()));
            }
        } else {
            holder.score.setText(R.string.vs);
        }

        if (position > 0) {
            holder.roundSeparator1.setVisibility(View.GONE);
            holder.roundSeparator2.setVisibility(View.GONE);
            holder.periodSeparator.setVisibility(View.GONE);
            if (data.get(position-1).getRound() != m.getRound()) {
                holder.roundSeparator1.setVisibility(View.VISIBLE);
                holder.roundSeparator2.setVisibility(View.VISIBLE);
            } else if (data.get(position - 1).getPeriod() != m.getPeriod()) {
                holder.periodSeparator.setVisibility(View.VISIBLE);
            }
        }

        String title = m.getHomeName()+" "+res.getString(R.string.vs)+" "+m.getAwayName();
        setOnClickListeners(holder.card, m, position, title);
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
