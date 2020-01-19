package fit.cvut.org.cz.bowling.presentation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
    private Fragment parentFrag;

    public MatchParticipantAdapter(Context ctx, Fragment f, TournamentType tournamentType) {
        this.context = ctx;
        this.parentFrag = f;
        this.tournamentType = tournamentType;
    }

    @NonNull
    @Override
    public MatchParticipantAdapter.MatchParticipantStatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MatchParticipantStatViewHolder(LayoutInflater.from(context).inflate(R.layout.row_participant, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MatchParticipantAdapter.MatchParticipantStatViewHolder holder, int i) {
        ParticipantOverview stats = data.get(i);
        String participantName = stats.getName();

        if(tournamentType.equals(TournamentTypes.individuals()))
        {
            holder.manage_participant.setVisibility(View.GONE );
        }
        else
        {
            holder.manage_participant.setVisibility(View.VISIBLE);
        }
        if(stats.getScore() < 0) //nobody played yet
        {
            holder.score_display.setVisibility(View.GONE);
            holder.warning_not_played_yet.setVisibility(View.VISIBLE);
            holder.place_lable.setVisibility(View.GONE);
            holder.final_score_text_view.setVisibility(View.GONE);
            holder.final_score_text_view.setText("");
        }
        else
        {
            holder.score_display.setVisibility(View.VISIBLE);
            holder.warning_not_played_yet.setVisibility(View.GONE);
            holder.place_lable.setVisibility(View.VISIBLE);
            holder.final_score_text_view.setVisibility(View.VISIBLE);
            holder.final_score_text_view.setText(String.format(Locale.getDefault(),"%d",i+1));
        }
        holder.as_score.setText(String.format(Locale.getDefault(), "%d", stats.getScore()));
        holder.participant_label.setText(participantName);

    }

    public class MatchParticipantStatViewHolder extends RecyclerView.ViewHolder{
        public long id;
        public View wholeView;
        LinearLayout score_display,warning_not_played_yet;
        TextView place_lable,final_score_text_view,participant_label,as_score;
        ImageView manage_participant;



        public MatchParticipantStatViewHolder(@NonNull View itemView) {

            super(itemView);

            score_display = itemView.findViewById(R.id.score_display);
            warning_not_played_yet = itemView.findViewById(R.id.warning_not_played_yet);
            place_lable = itemView.findViewById(R.id.place_label);
            final_score_text_view = itemView.findViewById(R.id.final_score_text_view);
            participant_label = itemView.findViewById(R.id.participant_label);
            as_score = itemView.findViewById(R.id.as_score);
            manage_participant = itemView.findViewById(R.id.manage_participant);

            wholeView = itemView;
        }
    }
}
