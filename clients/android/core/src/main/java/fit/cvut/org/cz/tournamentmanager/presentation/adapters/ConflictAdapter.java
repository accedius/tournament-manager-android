package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.entities.Conflict;
import fit.cvut.org.cz.tournamentmanager.business.entities.ConflictValue;

/**
 * Created by kevin on 24.10.2016.
 */
public class ConflictAdapter extends AbstractListAdapter<Conflict, ConflictAdapter.ConflictViewHolder> {
    private final Activity activity;
    private View v;
    private RecyclerView recyclerView;
    private AbstractListAdapter adapter;

    public ConflictAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ConflictAdapter.ConflictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conflict, parent, false);
        ConflictViewHolder holder = new ConflictViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ConflictAdapter.ConflictViewHolder holder, int position) {
        Conflict conflict = data.get(position);
        holder.title.setText(conflict.getTitle());

        recyclerView = (RecyclerView) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.recycler_view_conflict);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ConflictValue> conflictValues = conflict.getValues();
        adapter.swapData(conflictValues);
    }

    private AbstractListAdapter getAdapter() {
        return new ConflictValueAdapter();
    }

    public class ConflictViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public View wholeView;
        public ConflictViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            wholeView = itemView;
        }
    }

    public class ConflictValueAdapter extends AbstractListAdapter<ConflictValue, ConflictValueAdapter.ConflictValueViewHolder> {

        @Override
        public ConflictValueAdapter.ConflictValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conflict_value, parent, false);
            ConflictValueViewHolder holder = new ConflictValueViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ConflictValueAdapter.ConflictValueViewHolder holder, int position) {
            ConflictValue value = data.get(position);
            holder.attribute.setText(value.getAttribute());
            holder.leftValue.setText(value.getLeftValue());
            holder.rightValue.setText(value.getRightValue());
        }

        public class ConflictValueViewHolder extends RecyclerView.ViewHolder{
            public TextView attribute;
            public TextView leftValue;
            public TextView rightValue;

            public ConflictValueViewHolder(View itemView) {
                super(itemView);
                attribute = (TextView) itemView.findViewById(R.id.conflict_attribute);
                leftValue = (TextView) itemView.findViewById(R.id.conflict_left);
                rightValue = (TextView) itemView.findViewById(R.id.conflict_right);
            }
        }
    }
}
