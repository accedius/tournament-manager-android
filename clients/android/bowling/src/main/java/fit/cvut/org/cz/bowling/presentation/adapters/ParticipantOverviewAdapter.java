package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.ParticipantOverview;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class ParticipantOverviewAdapter extends AbstractListAdapter<ParticipantOverview, ParticipantOverviewAdapter.ParticipantOverviewViewHolder> {

    @Override
    public ParticipantOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParticipantOverviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match_participant_stats, parent, false));
    }

    protected void setOnClickListeners(View v, final long participantStatId, int position, String name, int score, byte framesPlayedNumber) {}

    @Override
    public void onBindViewHolder(ParticipantOverviewViewHolder holder, int position) {
        ParticipantOverview participantOverview = data.get(position);
        setOnClickListeners(holder.wholeView, participantOverview.getParticipantStatId(), position, participantOverview.getName(), participantOverview.getScore(), participantOverview.getFramesPlayedNumber());

        String participantName = participantOverview.getName();
        Integer score = participantOverview.getScore();
        Byte framesPlayedNumber = participantOverview.getFramesPlayedNumber();
    }

    public class ParticipantOverviewViewHolder extends RecyclerView.ViewHolder{
        TextView participantName;
        TextView framesPlayedNumber;
        TextView score;
        View wholeView;

        public ParticipantOverviewViewHolder(View itemView) {
            super(itemView);
            participantName = (TextView) itemView.findViewById(R.id.participant_label);
            framesPlayedNumber = (TextView) itemView.findViewById(R.id.played_frames_text_view);
            score = (TextView) itemView.findViewById(R.id.final_score_text_view);
            wholeView = itemView;
        }
    }
}
