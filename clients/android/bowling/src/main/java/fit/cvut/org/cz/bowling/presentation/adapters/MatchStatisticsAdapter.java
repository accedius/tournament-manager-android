package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.dialogs.PlayerMatchStatDialog;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class MatchStatisticsAdapter extends AbstractListAdapter<PlayerStat, MatchStatisticsAdapter.MatchStatisticsViewHolder> {
    private Fragment parentFrag;
    private boolean isHome = true;

    public void setIsHome(boolean home){
        isHome = home;
    }

    public MatchStatisticsAdapter(Fragment f) {
        this.parentFrag = f;
    }

    @Override
    public MatchStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_stats, parent, false));
    }

    /**
     * Method to bind data to View Holder
     * @param holder View Holder
     * @param position position of stats in data array
     */
    @Override
    public void onBindViewHolder(MatchStatisticsViewHolder holder, int position) {
        PlayerStat stats = data.get(position);
        holder.name.setText(stats.getName());
        holder.ST.setText(Long.toString(stats.getStrikes()));
        holder.SP.setText(Long.toString(stats.getSpares()));
        holder.PT.setText(Long.toString(stats.getPoints()));
        setOnClickListeners(holder.wholeView, stats.getPlayerId(), position, stats.getName());
    }

    private void setOnClickListeners(View v, long playerId, final int position, final String name) {
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PlayerMatchStatDialog dialog = PlayerMatchStatDialog.newInstance(data.get(position), position, isHome, name);
                dialog.setTargetFragment(parentFrag, 1);
                dialog.show(parentFrag.getFragmentManager(), "EDIT_DELETE_PLAYER_STATS");
                return true;
            }
        });
    }

    /**
     * removes stats from data
     * @param position position to be removed
     */
    public void removePos(int position){
        data.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Match Statistics specific View Holder
     */
    public class MatchStatisticsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public View wholeView;
        TextView name, ST, SP, PT;

        public MatchStatisticsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            ST = (TextView) itemView.findViewById(R.id.as_strikes);
            SP = (TextView) itemView.findViewById(R.id.as_spares);
            PT = (TextView) itemView.findViewById(R.id.as_points);
            wholeView = itemView;
        }
    }
}
