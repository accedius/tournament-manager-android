package fit.cvut.org.cz.bowling.presentation.adapters;


import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class BowlingMatchAdapter extends AbstractListAdapter<Match,BowlingMatchAdapter.MatchViewHolder> {
    protected Resources res;

    public BowlingMatchAdapter(Resources res) {
        this.res = res;
    }

    protected void setOnClickListeners(View v, Match match, int position, String title){}

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ffa_match, parent, false));
    }

    /**
     * Method to bind data to View Holder
     * @param holder View Holder
     * @param position position of match in data array
     */
    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match m = data.get(position);
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();

        //List<Participant> parts = m.getParticipants();
        //int players = 0;
        //for(Participant p : parts) if(p!=null) players += 1;
        holder.name.setText("Per. " + m.getPeriod() + " - Round " + m.getRound());
        holder.date.setText(m.getDate()!=null?dateFormat.format(m.getDate()):"[no Date]");

        /*
        if (m.isPlayed()) {
            //TODO consider using date/name?
            holder.score.setText(String.format("%d:%d", m.getHomeScore(), m.getAwayScore()));
        } else {
            holder.score.setText(R.string.vs);
        }
        */

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

        String title = m.getDate()!=null?dateFormat.format(m.getDate()):"[no Date]";
        setOnClickListeners(holder.card, m, position, title);
    }

    /**
     * Match specific View Holder
     */
    public class MatchViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;
        View periodSeparator, roundSeparator1, roundSeparator2, card;
        public MatchViewHolder(View itemView) {
            super (itemView);
            name = (TextView) itemView.findViewById(R.id.tv_matchname);
            date = (TextView) itemView.findViewById(R.id.tv_date);

            periodSeparator = itemView.findViewById(R.id.period_separator);
            roundSeparator1 = itemView.findViewById(R.id.round_separator);
            roundSeparator2 = itemView.findViewById(R.id.round_separator2);
            card = itemView.findViewById(R.id.card);
        }
    }
}