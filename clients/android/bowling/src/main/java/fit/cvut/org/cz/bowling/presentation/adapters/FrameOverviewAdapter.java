package fit.cvut.org.cz.bowling.presentation.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class FrameOverviewAdapter extends AbstractListAdapter<FrameOverview, FrameOverviewAdapter.FrameViewHolder> {

    private TournamentType type;

    public FrameOverviewAdapter(TournamentType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FrameViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_match_frame, viewGroup, false));
    }

    protected void setOnClickListeners(View v, int position, final long playerId, byte frameNumber, List<Byte> rolls, String playerName, int currentScore) {}

    private String formatNumber(int num) { return String.format(Locale.getDefault(),"%d", num); }
    private Byte getOrNull(List<Byte> list, int index) {
        return index < list.size() ? list.get(index) : null;
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder holder, int i) {
        FrameOverview frameOw = data.get(i);
        long playerId = frameOw.getPlayerId();
        byte frameNum = frameOw.getFrameNumber();
        List<Byte> rolls = frameOw.getRolls();
        String playerName = frameOw.getPlayerName();
        int currentScore = frameOw.getCurrentScore();
        setOnClickListeners(holder.wholeView, i, playerId, frameNum, rolls, playerName, currentScore);

        holder.frameLabel.setText(formatNumber(frameNum));
        holder.playerLabel.setText(playerName);
        Byte roll1 = getOrNull(rolls, 0);
        Byte roll2 = getOrNull(rolls, 1);
        Byte roll3 = getOrNull(rolls, 2);

        if (roll1 != null) {
            if(roll1 == 10){
                holder.roll1.setText(ExtraConstants.STRIKE_SYMBOL);
                if(frameNum!=10){
                    roll2 = null;
                }
            } else if (roll1 == 0) {
              holder.roll1.setText(ExtraConstants.ZERO_SYMBOL);
            } else {
                holder.roll1.setText(formatNumber(roll1));
            }
            holder.roll1.setVisibility(View.VISIBLE);
        }
        else {
            holder.roll1.setVisibility(View.GONE);
        }

        if (roll2 != null) {
            if(roll2 == 10) {
                holder.roll2.setText(ExtraConstants.STRIKE_SYMBOL);
            } else if (roll2 + roll1 == 10 && roll2 > 0) {
                holder.roll2.setText(ExtraConstants.SPARE_SYMBOL);
                holder.roll3.setVisibility(View.GONE);
            } else if (roll2 == 0) {
                holder.roll2.setText(ExtraConstants.ZERO_SYMBOL);
            } else {
                holder.roll2.setText(formatNumber(roll2));
            }
            holder.roll2.setVisibility(View.VISIBLE);
        }
        else {
            holder.roll2.setVisibility(View.GONE);
        }

        if (roll3 != null) {
            if(roll3 == 10) {
                holder.roll3.setText(ExtraConstants.STRIKE_SYMBOL);
            } else if (roll3 + roll2 == 10 && roll3 > 0 && roll1 + roll2 != 10) {
                holder.roll3.setText(ExtraConstants.SPARE_SYMBOL);
            } else if (roll3 == 0) {
                holder.roll3.setText(ExtraConstants.ZERO_SYMBOL);
            } else {
                holder.roll3.setText(formatNumber(roll3));
            }
            holder.roll3.setVisibility(View.VISIBLE);
        }
        else
            holder.roll3.setVisibility(View.GONE);

        holder.currentScore.setText(formatNumber(currentScore));
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder {

        TextView frameLabel, playerLabel, roll1, roll2, roll3, currentScore;
        View wholeView;

        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLabel = itemView.findViewById(R.id.frame_label);
            playerLabel = itemView.findViewById(R.id.player_label);
            roll1 = itemView.findViewById(R.id.throw_1_text_view);
            roll2 = itemView.findViewById(R.id.throw_2_text_view);
            roll3 = itemView.findViewById(R.id.throw_3_text_view);
            currentScore = itemView.findViewById(R.id.current_score_text_view);
            wholeView = itemView;
            if(type.equals(TournamentTypes.individuals())) {
                playerLabel.setVisibility(View.GONE);
            }
        }
    }
}
