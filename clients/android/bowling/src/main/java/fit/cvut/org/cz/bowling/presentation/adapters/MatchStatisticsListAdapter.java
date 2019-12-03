package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class MatchStatisticsListAdapter extends AbstractListAdapter<MatchStatisticsAdapter,MatchStatisticsListAdapter.StatisticsListViewHolder> {


    @NonNull
    @Override
    public StatisticsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatisticsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ffa_match,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsListViewHolder holder, int position) {
        holder.recyclerView.setAdapter(data.get(position));
    }

    public class StatisticsListViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public StatisticsListViewHolder(View itemView) {
            super (itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_list);
        }
    }
}
