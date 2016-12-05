package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.entities.ConflictValue;

/**
 * Created by kevin on 24.10.2016.
 */
public class ConflictAdapter extends AbstractListAdapter<Conflict, ConflictAdapter.ConflictViewHolder> {
    private Context context;
    private View v;
    private RecyclerView recyclerView;
    private AbstractListAdapter adapter;

    public ConflictAdapter(Context context) {
        this.context = context;
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
        holder.listener.position = position;

        holder.title.setText(conflict.getTitle());

        recyclerView = (RecyclerView) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.recycler_view_conflict);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
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
        public RadioGroup radioGroup;
        public EditActionListener listener;
        public ConflictViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            wholeView = itemView;
            listener = new EditActionListener();
            radioGroup.setOnCheckedChangeListener(listener);
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

    private class EditActionListener implements RadioGroup.OnCheckedChangeListener {
        public int position;

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.button_keep_local) {
                data.get(position).setAction(Conflict.KEEP_LOCAL);
            } else if (checkedId == R.id.button_take_file) {
                data.get(position).setAction(Conflict.TAKE_FILE);
            }
        }
    }
}
