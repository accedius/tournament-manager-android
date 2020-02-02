package fit.cvut.org.cz.bowling.presentation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.ParticipantOverview;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class MatchParticipantAdapter extends AbstractListAdapter<ParticipantOverview, MatchParticipantAdapter.MatchParticipantStatViewHolder> {
    private TournamentType tournamentType;
    private Context context;

    public MatchParticipantAdapter(Context ctx, TournamentType tournamentType) {
        this.context = ctx;
        this.tournamentType = tournamentType;
    }

    @NonNull
    @Override
    public MatchParticipantStatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MatchParticipantStatViewHolder(LayoutInflater.from(context).inflate(R.layout.row_participant, viewGroup, false));
    }

    protected void setOnClickListeners(View clickableView, View buttonView, ParticipantOverview overview, int position) {}

    @Override
    public void onBindViewHolder(@NonNull MatchParticipantStatViewHolder holder, int position) {
        ParticipantOverview stats = data.get(position);
        setOnClickListeners(holder.wholeView, holder.manage_participant, stats, position);
        String participantName = stats.getName();

        if(tournamentType.equals(TournamentTypes.individuals()))
        {
            holder.manage_participant.setVisibility(View.GONE );
        }
        else
        {
            holder.manage_participant.setVisibility(View.VISIBLE);
        }
        if(stats.getScore() < 0 || stats.getFramesPlayedNumber() <= 0) //nobody played yet
        {
            holder.score_display.setVisibility(View.GONE);
            holder.warning_not_played_yet.setVisibility(View.VISIBLE);
            holder.place_label.setVisibility(View.GONE);
            holder.final_score_text_view.setVisibility(View.GONE);
            holder.final_score_text_view.setText("");
        }
        else
        {
            holder.score_display.setVisibility(View.VISIBLE);
            holder.warning_not_played_yet.setVisibility(View.GONE);
            holder.place_label.setVisibility(View.VISIBLE);
            holder.final_score_text_view.setVisibility(View.VISIBLE);
            holder.final_score_text_view.setText(String.format(Locale.getDefault(),"%d",position+1));
        }
        holder.as_score.setText(String.format(Locale.getDefault(), "%d", stats.getScore()));
        holder.participant_label.setText(participantName);

    }

    public class MatchParticipantStatViewHolder extends RecyclerView.ViewHolder{
        public long id;
        public View wholeView;
        LinearLayout score_display,warning_not_played_yet;
        TextView place_label,final_score_text_view,participant_label,as_score;
        ImageView manage_participant;



        public MatchParticipantStatViewHolder(@NonNull View itemView) {

            super(itemView);

            score_display = itemView.findViewById(R.id.score_display);
            warning_not_played_yet = itemView.findViewById(R.id.warning_not_played_yet);
            place_label = itemView.findViewById(R.id.place_label);
            final_score_text_view = itemView.findViewById(R.id.final_score_text_view);
            participant_label = itemView.findViewById(R.id.participant_label);
            as_score = itemView.findViewById(R.id.as_score);
            manage_participant = itemView.findViewById(R.id.manage_participant);

            wholeView = itemView;
        }
    }
}
