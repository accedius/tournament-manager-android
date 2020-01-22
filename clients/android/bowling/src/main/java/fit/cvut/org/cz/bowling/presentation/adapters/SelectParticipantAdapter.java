package fit.cvut.org.cz.bowling.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;

public class SelectParticipantAdapter extends AbstractSelectableListAdapter<Participant, SimpleOneActionViewHolder> {
    @Override
    protected void bindView(SimpleOneActionViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
    }

    @Override
    public SimpleOneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(fit.cvut.org.cz.tmlibrary.R.layout.row_select_item, parent, false);
        return new SimpleOneActionViewHolder(v, this);
    }
}
