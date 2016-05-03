package fit.cvut.org.cz.hockey.presentation.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.dialogs.EditStatsDialog;
import fit.cvut.org.cz.hockey.presentation.dialogs.PlayerMatchStatDialog;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by atgot_000 on 23. 4. 2016.
 */
public class MatchStatisticsAdapter extends AbstractListAdapter<MatchPlayerStatistic, MatchStatisticsAdapter.MatchStatisticsViewHolder> {

    private Fragment parentFrag;

    public MatchStatisticsAdapter( Fragment f )
    {
        this.parentFrag = f;
    }

    @Override
    public MatchStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchStatisticsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchStatisticsViewHolder holder, int position) {
        MatchPlayerStatistic stats = data.get( position );
        holder.name.setText(stats.getName());
        holder.G.setText(Long.toString(stats.getGoals()));
        holder.A.setText(Long.toString(stats.getAssists()));
        holder.I.setText(Long.toString(stats.getInterventions()));
        holder.PMP.setText(Long.toString(stats.getPlusMinusPoints()));

        setOnClickListeners(holder.wholeView, stats.getPlayerId(), position);
    }

    private void setOnClickListeners( View v, long playerId, final int position )
    {
        v.setOnLongClickListener( new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                PlayerMatchStatDialog dialog = new PlayerMatchStatDialog(){
                    @Override
                    protected DialogInterface.OnClickListener supplyListener() {
                        return new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch ( which )
                                {
                                    case 0:
                                    {
                                        EditStatsDialog editDial = new EditStatsDialog() {
                                            @Override
                                            protected void saveStats(MatchPlayerStatistic statistic) {
                                                data.remove( position );
                                                data.add(position, statistic);
                                                notifyDataSetChanged();
                                            }
                                        };
                                        Bundle b = new Bundle();
                                        b.putParcelable( EditStatsDialog.ARG_STATS, data.get(position) );
                                        editDial.setArguments( b );
                                        editDial.show(parentFrag.getFragmentManager(), "EDIT_STATS_DIAL");
                                        break;
                                    }
                                    case 1:
                                    {
                                        data.remove(position);
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }
                                dialog.dismiss();
                            }
                        };
                    }
                };
                dialog.show(parentFrag.getFragmentManager(), "EDIT_DELETE_PLAYER_STATS");

                return true;
            }
        });
    }

    public class MatchStatisticsViewHolder extends RecyclerView.ViewHolder
    {
        public long id;
        public View wholeView;
        TextView name, G, A, PMP, I;

        public MatchStatisticsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.as_name);
            G = (TextView) itemView.findViewById(R.id.as_goals);
            A = (TextView) itemView.findViewById(R.id.as_assists);
            PMP = (TextView) itemView.findViewById(R.id.as_pmp);
            I = (TextView) itemView.findViewById(R.id.as_interventions);
            wholeView = itemView;

        }
    }
}
