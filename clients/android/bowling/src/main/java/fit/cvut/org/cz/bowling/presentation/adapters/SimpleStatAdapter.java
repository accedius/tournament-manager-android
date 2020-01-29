package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class SimpleStatAdapter extends AbstractListAdapter<PlayerStat, SimpleStatAdapter.PlayerStatOverviewViewHolder> {

    public void setItemAtPosition(int position, PlayerStat playerStat) {
        data.set(position, playerStat);
    }

    @NonNull
    @Override
    public PlayerStatOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new PlayerStatOverviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_match_simple_stat, parent, false));
    }

    protected void setOnClickListeners(View v, PlayerStat overview, int position) {}

    private String formatNumber(int num) { return String.format(Locale.getDefault(),"%d", num); }

    @Override
    public void onBindViewHolder(@NonNull PlayerStatOverviewViewHolder holder, int position) {
        PlayerStat overview = data.get(position);
        setOnClickListeners(holder.clickableView, new PlayerStat(overview), position);

        int strikes, spares, points, frames;
        String name;
        frames = overview.getFramesPlayedNumber();
        name = overview.getName();
        holder.name.setText(name);
        if(frames > 0) {
            strikes = overview.getStrikes();
            spares = overview.getSpares();
            points = overview.getPoints();
            holder.score.setText(formatNumber(points));
            holder.frames.setText(formatNumber(frames));
            holder.spares.setText(formatNumber(spares));
            holder.strikes.setText(formatNumber(strikes));
            if(holder.scoreLayout.getVisibility() != View.VISIBLE) {
                holder.scoreLayout.setVisibility(View.VISIBLE);
                holder.dataLayout.setVisibility(View.VISIBLE);
                holder.warningLayout.setVisibility(View.GONE);
            }
        } else {
            if(holder.scoreLayout.getVisibility() != View.GONE) {
                holder.scoreLayout.setVisibility(View.GONE);
                holder.dataLayout.setVisibility(View.GONE);
                holder.warningLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    class PlayerStatOverviewViewHolder extends RecyclerView.ViewHolder {
        TextView name, score, frames, spares, strikes;
        LinearLayout dataLayout, warningLayout, scoreLayout;
        View clickableView;

        public PlayerStatOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.player_label);
            score = itemView.findViewById(R.id.as_score);
            frames = itemView.findViewById(R.id.as_frames);
            spares = itemView.findViewById(R.id.as_spares);
            strikes = itemView.findViewById(R.id.as_strikes);

            scoreLayout = itemView.findViewById(R.id.score_layout);
            dataLayout = itemView.findViewById(R.id.player_data_layout);
            warningLayout = itemView.findViewById(R.id.warning_not_played_yet);
            clickableView = itemView;
        }
    }
}
